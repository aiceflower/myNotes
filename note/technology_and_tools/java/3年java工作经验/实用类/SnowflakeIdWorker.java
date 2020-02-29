package com.kankan.module.microvision.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.System.currentTimeMillis;

/**
 * 雪花算法(snowflake)-生成分布式自增id
 * SnowFlake的结构:
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)得到的值。
 * 这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */

/**
 * @author aiceflower
 */
public class SnowflakeIdWorker {
    /**开始时间截*/
    private  final long twepoch = 1560268800000L;

    /**机器id所占的位数*/
    private final long workerIdBits = 5L;
    /**数据标识id所占的位数*/
    private final long datacenterIdBits = 5L;
    /**支持的最大机器id，结果是31 （这个位移算法可以很快的计算出几位二进制数所能表示的最大十进制数）*/
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /**支持的最大数据标识id，结果是31*/
    private final long maxDatacenterId = -1L ^ (-1L << workerIdBits);
    /**序列在id中占的位数*/
    private final long sequenceBits = 12L;
    /**机器id所左移12位*/
    private final long workerIdShift = sequenceBits;
    /** 数据标识id向左移17位(12+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    /** 工作机器ID(0~31) */
    private long workerId;
    /** 数据中心ID(0~31) */
    private long datacenterId;
    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;
    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    public SnowflakeIdWorker(long workerId, long datacenterId){
        if (workerId > maxWorkerId || workerId < 0){
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0){
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", datacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }
    /**
     * 获取下一个id，线程安全
     * @return
     */
    public synchronized long nextId(){
        //当前毫秒
        long timestamp = currentTimeMillis();
        //当前时间小于上次生成id时间，说明系统时钟回退，抛出异常
        if (timestamp < lastTimestamp){
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp){
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0){
                timestamp = tillNextMillis(lastTimestamp);
            }
        }else {//时间戳改变，毫秒内序列重置
            sequence = 0;
        }

        //上次生成的时间戳
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tillNextMillis(long lastTimestamp){
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp){
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }
    public static void main(String[] args){
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("雪花算法生成id线程--%d--").build();
        ExecutorService poolExecutor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),factory);

        for (int i = 0; i < 10; i++) {
            poolExecutor.execute(() -> {
                Random random = new Random();
                SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(random.nextInt(31),random.nextInt(31));
                for (int j = 0; j < 10; j++) {
                    long id = snowflakeIdWorker.nextId();
                    System.out.println(Thread.currentThread().getName() + id);
                }
            });
        }
        poolExecutor.shutdown();
    }
}
