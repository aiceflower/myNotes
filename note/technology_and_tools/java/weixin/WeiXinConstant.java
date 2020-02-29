package com.weixin.demo.constant;

/**
 * 微信各功能请求地址
 */
public class WeiXinConstant {

    /**
     * 获取access_token url
     */
    public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    /**
     * 获取菜单url
     */
    public static final String GET_MENU = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";

    /**
     * 添加自定义菜单，post请求
     */
    public static final String ADD_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";

    /**
     * 发送模板消息url
     */
    public static final String SEND_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    /**
     * ytxx appid
     */
    public static final String YTXX_APPID = "wx79c0f9f9c473f6bc";//+"test";

    /**
     * ytxx secret
     */
    public static final String YTXX_SECRET = "f053c5cc47bb9baeeb9d6ac9b0f226d4";//+"test";

    /**
     * test appid
     */
    public static final String TEST_APPID = "wx4b93a71cb5d8a69b";

    /**
     * test secret
     */
    public static final String TEST_SECRET = "f95c5d4a58f8551f36df9e66c5a742f9";

    /**
     * test menu info
     */
    public static final String TEST_MENU = "{\"button\":[{\"name\":\"开心一刻开\",\"sub_button\":[{\"type\":\"view\",\"name\":\"百度一下百度一下百度一下百度一下百度一下\",\"url\":\"http://m.baidu.com\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"慕课\",\"url\":\"http://m.imooc.com\",\"sub_button\":[]}]},{\"name\":\"买买买买\",\"sub_button\":[{\"type\":\"view\",\"name\":\"淘宝网淘宝网淘宝网淘宝网淘宝网淘宝网刚刚\",\"url\":\"http://m.taobao.com\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"hello World\",\"url\":\"http://111.230.150.224/hello\",\"sub_button\":[]}]},{\"name\":\"没反应啊\",\"type\":\"click\",\"key\":\"abc\"}]}";

    /**
     * 待办任务提醒模板ID
     */
    public static final String SEND_PENDING_TASK_TEMPLATE_ID = "M42HEkuWyrvEy4kJyTlQIzsGctLFi5wUnRQslbhAJfQ";

    /**
     * 测试用户openid
     */
    public static final String TEST_USER_OPENID = "oGZwcuPwD6BbMJgYir0wlpavJMCY";

    /**
     * 测试消息内容
     */
    public static final String TEST_TEMPLATE_CONTENT = "{\"touser\":\"%s\",\"template_id\":\"%s\",\"data\":{\"first\":{\"value\":\"欢迎使用运通信息微信公众号！\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"模板测试\",\"color\":\"#00ff00\"},\"keyword2\":{\"value\":\"没有\",\"color\":\"#ff0ff0\"},\"remark\":{\"value\":\"欢迎下次再来！\",\"color\":\"#ff00ff\"}}}";
}
