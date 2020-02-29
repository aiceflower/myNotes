package com.mg.tools.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mg.util.core.message.ServerConfig;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * 敏感词处理工具 - DFA算法实现
 *
 * @author sam
 * @since 2017/9/4
 */

public class SensitiveWordUtil {


        /**
         * 敏感词匹配规则
         */
        public static final int MinMatchTYpe = 1;      //最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
        public static final int MaxMatchType = 2;      //最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]

        /**
         * 敏感词集合
         */
        public static HashMap sensitiveWordMap;

        /**
         * 初始化敏感词库，构建DFA算法模型
         *
         * @param sensitiveWordSet 敏感词库
         */
        public static synchronized void init(Set<String> sensitiveWordSet) {
            initSensitiveWordMap(sensitiveWordSet);
        }
        /**
         * 初始化敏感词库，构建DFA算法模型
         *
         *
         */
        public static synchronized void init() {
            if(CollectionUtil.isNotEmpty(sensitiveWordMap)){
                return;
            }
            String SensitiveWord=ServerConfig.getInstance().getConfig("sensitiveWord");
            if(StringUtils.isNotEmpty(SensitiveWord)){
                List<String> list=StrUtil.splitTrim(SensitiveWord,",");
                initSensitiveWordMap(list);
            }else {
                initSensitiveWordMap(new ArrayList<>());

            }
        }
        /**
         * 初始化敏感词库，构建DFA算法模型
         *
         * @param sensitiveWordSet 敏感词库
         */
        private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
            //初始化敏感词容器，减少扩容操作
            sensitiveWordMap = new HashMap(sensitiveWordSet.size());
            String key;
            Map nowMap;
            Map<String, String> newWorMap;
            //迭代sensitiveWordSet
            Iterator<String> iterator = sensitiveWordSet.iterator();
            while (iterator.hasNext()) {
                //关键字
                key = iterator.next();
                nowMap = sensitiveWordMap;
                for (int i = 0; i < key.length(); i++) {
                    //转换成char型
                    char keyChar = key.charAt(i);
                    //库中获取关键字
                    Object wordMap = nowMap.get(keyChar);
                    //如果存在该key，直接赋值，用于下一个循环获取
                    if (wordMap != null) {
                        nowMap = (Map) wordMap;
                    } else {
                        //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                        newWorMap = new HashMap<>();
                        //不是最后一个
                        newWorMap.put("isEnd", "0");
                        nowMap.put(keyChar, newWorMap);
                        nowMap = newWorMap;
                    }

                    if (i == key.length() - 1) {
                        //最后一个
                        nowMap.put("isEnd", "1");
                    }
                }
            }
        }
    private static void initSensitiveWordMap(List<String> sensitiveWordSet) {
        //初始化敏感词容器，减少扩容操作
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代sensitiveWordSet
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            //关键字
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);
                //库中获取关键字
                Object wordMap = nowMap.get(keyChar);
                //如果存在该key，直接赋值，用于下一个循环获取
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<>();
                    //不是最后一个
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    //最后一个
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

        /**
         * 判断文字是否包含敏感字符
         *
         * @param txt       文字
         * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
         * @return 若包含返回true，否则返回false
         */
        public static boolean contains(String txt, int matchType) {
            boolean flag = false;
            for (int i = 0; i < txt.length(); i++) {
                int matchFlag = checkSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
                if (matchFlag > 0) {    //大于0存在，返回true
                    flag = true;
                }
            }
            return flag;
        }

        /**
         * 判断文字是否包含敏感字符
         *
         * @param txt 文字
         * @return 若包含返回true，否则返回false
         */
        public static boolean contains(String txt) {
            return contains(txt, MaxMatchType);
        }

        /**
         * 获取文字中的敏感词
         *
         * @param txt       文字
         * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
         * @return
         */
        public static Set<String> getSensitiveWord(String txt, int matchType) {
            Set<String> sensitiveWordList = new HashSet<>();

            for (int i = 0; i < txt.length(); i++) {
                //判断是否包含敏感字符
                int length = checkSensitiveWord(txt, i, matchType);
                if (length > 0) {//存在,加入list中
                    sensitiveWordList.add(txt.substring(i, i + length));
                    i = i + length - 1;//减1的原因，是因为for会自增
                }
            }

            return sensitiveWordList;
        }

        /**
         * 获取文字中的敏感词
         *
         * @param txt 文字
         * @return
         */
        public static Set<String> getSensitiveWord(String txt) {
            return getSensitiveWord(txt, MaxMatchType);
        }

        /**
         * 替换敏感字字符
         *
         * @param txt         文本
         * @param replaceChar 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
         * @param matchType   敏感词匹配规则
         * @return
         */
        public static String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
            String resultTxt = txt;
            //获取所有的敏感词
            Set<String> set = getSensitiveWord(txt, matchType);
            Iterator<String> iterator = set.iterator();
            String word;
            String replaceString;
            while (iterator.hasNext()) {
                word = iterator.next();
                replaceString = getReplaceChars(replaceChar, word.length());
                resultTxt = resultTxt.replaceAll(word, replaceString);
            }

            return resultTxt;
        }

        /**
         * 替换敏感字字符
         *
         * @param txt         文本
         * @param replaceChar 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
         * @return
         */
        public static String replaceSensitiveWord(String txt, char replaceChar) {
            return replaceSensitiveWord(txt, replaceChar, MaxMatchType);
        }

        /**
         * 替换敏感字字符
         *
         * @param txt        文本
         * @param replaceStr 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
         * @param matchType  敏感词匹配规则
         * @return
         */
        public static String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
            String resultTxt = txt;
            //获取所有的敏感词
            Set<String> set = getSensitiveWord(txt, matchType);
            Iterator<String> iterator = set.iterator();
            String word;
            while (iterator.hasNext()) {
                word = iterator.next();
                resultTxt = resultTxt.replaceAll(word, replaceStr);
            }

            return resultTxt;
        }
    /**
     * 替换敏感字字符
     *
     * @param txt        文本
     * @param replaceStr 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
     * @param matchType  敏感词匹配规则
     * @return
     */
    public static String alterSensitiveWord(String txt, String replaceStr, int matchType) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replace(word, StrUtil.concat(true,replaceStr,word,replaceStr));
        }

        return resultTxt;
    }


        /**
         * 替换敏感字字符
         *
         * @param txt        文本
         * @param replaceStr 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
         * @return
         */
        public static String replaceSensitiveWord(String txt, String replaceStr) {
            return replaceSensitiveWord(txt, replaceStr, MaxMatchType);
        }

        /**
         * 获取替换字符串
         *
         * @param replaceChar
         * @param length
         * @return
         */
        private static String getReplaceChars(char replaceChar, int length) {
            String resultReplace = String.valueOf(replaceChar);
            for (int i = 1; i < length; i++) {
                resultReplace += replaceChar;
            }

            return resultReplace;
        }

        /**
         * 检查文字中是否包含敏感字符，检查规则如下：<br>
         *
         * @param txt
         * @param beginIndex
         * @param matchType
         * @return 如果存在，则返回敏感词字符的长度，不存在返回0
         */
        private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
            //敏感词结束标识位：用于敏感词只有1位的情况
            boolean flag = false;
            //匹配标识数默认为0
            int matchFlag = 0;
            char word;
            Map nowMap = sensitiveWordMap;
            for (int i = beginIndex; i < txt.length(); i++) {
                word = txt.charAt(i);
                //获取指定key
                nowMap = (Map) nowMap.get(word);
                if (nowMap != null) {//存在，则判断是否为最后一个
                    //找到相应key，匹配标识+1
                    matchFlag++;
                    //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    if ("1".equals(nowMap.get("isEnd"))) {
                        //结束标志位为true
                        flag = true;
                        //最小规则，直接返回,最大规则还需继续查找
                        if (MinMatchTYpe == matchType) {
                            break;
                        }
                    }
                } else {//不存在，直接返回
                    break;
                }
            }
            if (matchFlag < 2 || !flag) {//长度必须大于等于1，为词
                matchFlag = 0;
            }
            return matchFlag;
        }



        public static void main(String[] args) throws Exception {

//            Set<String> sensitiveWordSet = new HashSet<>();
//            sensitiveWordSet.add("太多");
//            sensitiveWordSet.add("爱恋");
//            sensitiveWordSet.add("爱情");
//            sensitiveWordSet.add("爱心");
//            sensitiveWordSet.add("爱护");
//            sensitiveWordSet.add("静静");
//            sensitiveWordSet.add("哈哈");
//            sensitiveWordSet.add("啦啦");
//            sensitiveWordSet.add("感动");
//            sensitiveWordSet.add("发呆");
            //初始化敏感词库
            String string = "太多的伤感情怀也许只局限于饲养基地ssr妖靈+拓印 荧幕中的情节貨到付款";
//            string+=string;
//            string+=string;
//            string+=string;
//            string+=string;
            SensitiveWordUtil.sensitiveWordMap=null;
            SensitiveWordUtil.init();
//            String word=ServerConfig.getInstance().getConfig("sensitiveWord");
//            String[] words=word.split(",");
//            for (int i = 0; i <10 ; i++) {
//                long time=System.currentTimeMillis();
//                for (int j = 0; j <100000 ; j++) {
//                    for (String s:words) {
//                        string.replace(s,"*");
//                    }
//                }
//                System.out.println(DateUtil.spendMs(time));
//
//            }

//            for (int j = 0; j < 10; j++) {
                long time=System.currentTimeMillis();
                for (int i = 0; i <1 ; i++) {
//                System.out.println(                SensitiveWordUtil.sensitiveWordMap);
                    String filterStr = SensitiveWordUtil.alterSensitiveWord(string,"**",SensitiveWordUtil.MinMatchTYpe);
//                    String filterStr = SensitiveWordUtil.replaceSensitiveWord(string,"*",SensitiveWordUtil.MinMatchTYpe);
                    System.out.println(filterStr);
                }
                System.out.println(DateUtil.spendMs(time));

//            }

            System.out.println("敏感词的数量：" + SensitiveWordUtil.sensitiveWordMap.size());

//                    + "然后我们的扮演的角色就是跟随着微信號主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
//                    + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡微信號复制器一个贱人一杯红酒一部电影在夜 深人静的晚上，关上电话静静的发呆着。";
            System.out.println("待检测语句字数：" + string.length());
//
//            //是否含有关键字
//            boolean result = SensitiveWordUtil.contains(string);
//            System.out.println(result);
            boolean result = SensitiveWordUtil.contains(string, SensitiveWordUtil.MinMatchTYpe);
            System.out.println(result);
//            String filterStr = SensitiveWordUtil.replaceSensitiveWord(string,"*",SensitiveWordUtil.MinMatchTYpe);

            //获取语句中的敏感词
//            Set<String> set = SensitiveWordUtil.getSensitiveWord(string);
//            System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
//            set = SensitiveWordUtil.getSensitiveWord(string, SensitiveWordUtil.MinMatchTYpe);
//            System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
            //敏感词特殊显示
              String filterStr = SensitiveWordUtil.alterSensitiveWord(string,"**",SensitiveWordUtil.MinMatchTYpe);
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("- ").append("聊天内容:").append(filterStr).append("\n");
            stringBuffer.append("- ").append("游戏:").append("twsd").append("\n");
            stringBuffer.append("- ").append("游戏服:").append("111").append("\n");
            stringBuffer.append("- ").append("角色id:").append("22222").append("\n");
//            JSONObject jsonObject=DingDingSendUtil.returnMarkdownMessageContext("语句中包含敏感词",stringBuffer.toString());
//            System.out.println(jsonObject);
//            DingDingSendUtil.sendMarkdownMessage(jsonObject,"sensitiveWordWebHook");
//            System.out.println(filterStr);
//            System.out.println(string.replace("ssr妖靈+拓印", StrUtil.concat(true,"**","ssr妖靈+拓印","**")));
            //替换语句中的敏感词
//            String filterStr = SensitiveWordUtil.replaceSensitiveWord(string, '*');
//            System.out.println(filterStr);
//            filterStr = SensitiveWordUtil.replaceSensitiveWord(string, '*', SensitiveWordUtil.MinMatchTYpe);
//            System.out.println(filterStr);
//
//            String filterStr2 = SensitiveWordUtil.replaceSensitiveWord(string, "[*敏感词*]");
//            System.out.println(filterStr2);
//            filterStr2 = SensitiveWordUtil.replaceSensitiveWord(string, "[*敏感词*]", SensitiveWordUtil.MinMatchTYpe);
//            System.out.println(filterStr2);
        }

}
