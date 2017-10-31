package collback;

/**
 * 异步回调，java实现
 servlet中的filter也是使用的回调机制
 */
public class AsyncCallBack {
    public static void main(String[] args) {
        Server server = new Server();
        Client client = new Client(server);
        client.sendMsg("Server Hello.");
    }
}
interface CSCallBack {
    void process(String status);
}

class Server {

    public void getClientMsg(CSCallBack callBack,String msg){
        System.out.println("服务端:服务端接收到客户端消息:"+msg);

        try{
            Thread.sleep(5 * 1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("服务端处理数据成功，返回状态200");
        String status = "200";
        callBack.process(status);
    }
}

class Client implements CSCallBack{

    Server server = null;

    public Client(Server server){
        this.server = server;
    }

    public void sendMsg(final String msg){//内部内调用外部变量需要final
        System.out.println("客户端:发送消息为："+msg);
        new Thread(()->server.getClientMsg(Client.this,msg)).start();
        //server.getClientMsg(Client.this,msg);
        System.out.println("客户端：异步发成功");
    }

    @Override
    public void process(String status) {
        System.out.println("客户端:服务端回调状态为:"+status);
    }
}
