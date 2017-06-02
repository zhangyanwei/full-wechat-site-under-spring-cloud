package com.askdog.bootstrap;

import com.askdog.bootstrap.core.DataCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.askdog.model.entity.builder.TemplateBuilder.mailTemplateBuilder;
import static com.google.common.base.Joiner.on;
import static java.util.Locale.SIMPLIFIED_CHINESE;

@Service
@Order(3)
@Profile("initialize")
public class TemplateInitializer {

    @Autowired
    private DataCreator dataCreator;

    @PostConstruct
    private void initialize() {
        dataCreator
                .template(
                        mailTemplateBuilder()
                                .name("registration-confirm")
                                .subject("[ASKDOG] 注册确认")
                                .content("<!DOCTYPE html>\n" +
                                        "<html>\n" +
                                        "<head>\n" +
                                        "    <meta charset=\"UTF-8\">\n" +
                                        "    <title>Ask.Dog Register</title>\n" +
                                        "    <style type=\"text/css\">\n" +
                                        "        body {\n" +
                                        "           background-color: #f2f4f9;\n" +
                                        "        }\n" +
                                        "    </style>\n" +
                                        "</head>\n" +
                                        "<body bgcolor=\"#f2f4f9\">\n" +
                                        "<div style=\"display: table;margin-left: auto;margin-right: auto;\">\n" +
                                        "    <div style=\"height: 490px;width: 754px;background-color: #ffffff;margin-top: 40px;border-radius: 4px;box-shadow: 3px 3px 6px 0 #e3e6ec;font-size: 10px;border: solid 1px #e2e7f2\">\n" +
                                        "        <div>\n" +
                                        "            <img style=\"margin-top: 64px;margin-left: 7.6%;margin-bottom: 66px;width: 152px;\" src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        </div>\n" +
                                        "        <div style=\"font-family: 'Microsoft YaHei';\">\n" +
                                        "            <div style=\"margin-left: 7.3%;\">\n" +
                                        "                <h1 style=\"margin-top: 0;margin-bottom: 0;font-size: 1.8em;\">\n" +
                                        "                    感谢您注册ASKDOG!请点击以下按钮激活账户\n" +
                                        "                </h1>\n" +
                                        "                <button style=\"margin-top: 32px;margin-bottom: 32px;width: 96px;height: 46px;background-color: #20b5ff;border: none;border-radius: 4px;text-align: center;vertical-align: middle; font-size: 10px;font-family: 'Microsoft Yahei'; background-image: none;\">\n" +
                                        "                    <a style=\"color: #ffffff;line-height: 20px;text-decoration: none;font-size: 1.8em; letter-spacing: 0.3em\"  href=\"${url}\">\n" +
                                        "                        激活\n" +
                                        "                    </a>\n" +
                                        "                </button>\n" +
                                        "                <p style=\" margin-top: 0;margin-bottom: 0;font-size: 1.6em;\">\n" +
                                        "                    如果按钮无法点击，请将以下地址复制到浏览器打开\n" +
                                        "                </p>\n" +
                                        "                <a style=\"margin-top: 20px;margin-bottom: 20px;font-size: 1.6em;display: block;color: #20b5ff;\" href=\"${url}\">\n" +
                                        "                    ${url}\n" +
                                        "                </a>\n" +
                                        "            </div>\n" +
                                        "            <div>\n" +
                                        "                <hr style=\" margin-top: 0;margin-bottom: 0;color: #e2e7f2;border: 1px solid;width:100%;\"/>\n" +
                                        "                <p style=\" margin-left: 7.3%;margin-top: 20px;margin-bottom: 0;font-size: 1.6em;\">如有任何疑问，欢迎联系<a style=\"color: #00c4ff;text-decoration: none\" href=\"mailto:feedback@askdog.com\">feedback@askdog.com</a></p>\n" +
                                        "            </div>\n" +
                                        "        </div>\n" +
                                        "    </div>\n" +
                                        "    <div style=\"margin-top: 25px;margin-left: auto;margin-right: auto;text-align: center;font-size: 10px;font-family: 'Microsoft YaHei';\">\n" +
                                        "        <img style=\"width: 152px\" src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        <p style=\"margin-top: 12px;margin-bottom: 2px;font-size: 1.4em;\">版权所有©大连鸿影软件科技</p>\n" +
                                        "        <p style=\" margin: 0;font-size: 1.4em;\">Copyright©2016 ASKDOG</p>\n" +
                                        "    </div>\n" +
                                        "</div>\n" +
                                        "</body>\n" +
                                        "</html>")
                                .language(SIMPLIFIED_CHINESE)
                                .build()
                )
                .template(
                        mailTemplateBuilder()
                                .name("password-recover")
                                .subject("[ASKDOG] 找回密码")
                                .content("<!DOCTYPE html>\n" +
                                        "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                                        "<head>\n" +
                                        "    <meta charset=\"UTF-8\">\n" +
                                        "    <title>password-recover</title>\n" +
                                        "    <style type=\"text/css\">\n" +
                                        "        body {\n" +
                                        "            background-color: #f2f4f9;\n" +
                                        "        }\n" +
                                        "    </style>\n" +
                                        "</head>\n" +
                                        "<body bgcolor=\"#f2f4f9\">\n" +
                                        "<div style=\"display: table;margin-left: auto;margin-right: auto;\">\n" +
                                        "    <div style=\"width: 754px;background-color: #ffffff;margin-top: 40px;border-radius: 4px;box-shadow: 3px 3px 6px 0 #e3e6ec;border: solid 1px #e2e7f2; padding: 0 0 70px 0\">\n" +
                                        "        <div>\n" +
                                        "            <img style=\"margin-top: 64px;margin-left: 7.6%;margin-bottom: 66px;width: 152px;\"\n" +
                                        "                 src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        </div>\n" +
                                        "        <div style=\"font-family: 'Microsoft YaHei';\">\n" +
                                        "            <div style=\"margin-left: 7.3%;font-size: 16px\">\n" +
                                        "                <h1 style=\"margin-top: 0;margin-bottom: 0;font-size: 18px;\">\n" +
                                        "                    ${user.name}, 您好:\n" +
                                        "                </h1>\n" +
                                        "                <p style=\"font-size: 16px; line-height: 150%\">\n" +
                                        "                    您正在试图通过邮箱找回您的密码，如果不是您本人操作，请忽略。<br/>\n" +
                                        "                    继续找回您的密码，请点击下面的链接重置。\n" +
                                        "                    <a style=\"display: block;color: #20b5ff;\" href=\"${url}\">\n" +
                                        "                        ${url}\n" +
                                        "                    </a>\n" +
                                        "                    (如果链接不能点击，请复制并粘贴到浏览器的地址栏，然后按回车键)\n" +
                                        "                </p>\n" +
                                        "            </div>\n" +
                                        "            <div style=\"font-size: 16px\">\n" +
                                        "                <hr style=\" margin-top: 30px;margin-bottom: 0;color: #e2e7f2;border: 1px solid;width:100%;\"/>\n" +
                                        "                <p style=\" margin-left: 7.3%;margin-top: 20px;margin-bottom: 0;\">\n" +
                                        "                    如有任何疑问，欢迎联系\n" +
                                        "                    <a style=\"color: #00c4ff;text-decoration: none\" href=\"mailto:feedback@askdog.com\">feedback@askdog.com</a>\n" +
                                        "                </p>\n" +
                                        "            </div>\n" +
                                        "        </div>\n" +
                                        "    </div>\n" +
                                        "    <div style=\"margin-top: 25px;margin-left: auto;margin-right: auto;text-align: center;font-size: 10px;font-family: 'Microsoft YaHei';\">\n" +
                                        "        <img style=\"width: 152px\" src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        <p style=\"margin-top: 12px;margin-bottom: 2px;font-size: 1.4em;line-height: 18px\">版权所有©大连鸿影软件科技</p>\n" +
                                        "        <p style=\" margin: 0;font-size: 1.4em;line-height: 18px\">Copyright©2016 ASKDOG</p>\n" +
                                        "    </div>\n" +
                                        "</div>\n" +
                                        "</body>\n" +
                                        "</html>")
                                .language(SIMPLIFIED_CHINESE)
                                .build()
                )
                .template(
                        mailTemplateBuilder()
                                .name("notification-join-goldbucket")
                                .subject("[ASKDOG] 欢迎加入一桶金计划")
                                .content("<!DOCTYPE html>\n" +
                                        "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                                        "<head>\n" +
                                        "    <meta charset=\"UTF-8\">\n" +
                                        "    <title>notification-join-goldbucket</title>\n" +
                                        "    <style type=\"text/css\">\n" +
                                        "        body {\n" +
                                        "            background-color: #f2f4f9;\n" +
                                        "        }\n" +
                                        "    </style>\n" +
                                        "</head>\n" +
                                        "<body bgcolor=\"#f2f4f9\">\n" +
                                        "<div style=\"display: table;margin-left: auto;margin-right: auto;\">\n" +
                                        "    <div style=\"width: 754px;background-color: #ffffff;margin-top: 40px;border-radius: 4px;box-shadow: 3px 3px 6px 0 #e3e6ec;border: solid 1px #e2e7f2; padding: 0 0 70px 0\">\n" +
                                        "        <div>\n" +
                                        "            <img style=\"margin-top: 64px;margin-left: 7.6%;margin-bottom: 66px;width: 152px;\"\n" +
                                        "                 src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        </div>\n" +
                                        "        <div style=\"font-family: 'Microsoft YaHei';\">\n" +
                                        "            <div style=\"margin-left: 7.3%;font-size: 16px\">\n" +
                                        "                <h1 style=\"margin-top: 0;margin-bottom: 0;font-size: 18px;\">\n" +
                                        "                    ${user.name}, 您好:\n" +
                                        "                </h1>\n" +
                                        "                <p style=\"font-size: 16px; line-height: 150%\">\n" +
                                        "                    欢迎您加入一桶金计划，您的VIP图标已经点亮，现在就可以开启您的ASKDOG之旅了。\n" +
                                        "                </p>\n" +
                                        "            </div>\n" +
                                        "            <div style=\"font-size: 16px\">\n" +
                                        "                <hr style=\" margin-top: 30px;margin-bottom: 0;color: #e2e7f2;border: 1px solid;width:100%;\"/>\n" +
                                        "                <p style=\" margin-left: 7.3%;margin-top: 20px;margin-bottom: 0;\">\n" +
                                        "                    如有任何疑问，欢迎联系\n" +
                                        "                    <a style=\"color: #00c4ff;text-decoration: none\" href=\"mailto:feedback@askdog.com\">feedback@askdog.com</a>\n" +
                                        "                </p>\n" +
                                        "            </div>\n" +
                                        "        </div>\n" +
                                        "    </div>\n" +
                                        "    <div style=\"margin-top: 25px;margin-left: auto;margin-right: auto;text-align: center;font-size: 10px;font-family: 'Microsoft YaHei';\">\n" +
                                        "        <img style=\"width: 152px\" src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        <p style=\"margin-top: 12px;margin-bottom: 2px;font-size: 1.4em;line-height: 18px\">版权所有©大连鸿影软件科技</p>\n" +
                                        "        <p style=\" margin: 0;font-size: 1.4em;line-height: 18px\">Copyright©2016 ASKDOG</p>\n" +
                                        "    </div>\n" +
                                        "</div>\n" +
                                        "</body>\n" +
                                        "</html>>")
                                .language(SIMPLIFIED_CHINESE)
                                .build()
                )
                .template(
                        mailTemplateBuilder()
                                .name("notification-reject-goldbucket")
                                .subject("[ASKDOG] 请重新提交一桶金计划申请")
                                .content("<!DOCTYPE html>\n" +
                                        "<html xmlns=\"http://www.w3.org/1999/html\">\n" +
                                        "<head>\n" +
                                        "    <meta charset=\"UTF-8\">\n" +
                                        "    <title>notification-reject-goldbucket</title>\n" +
                                        "    <style type=\"text/css\">\n" +
                                        "        body {\n" +
                                        "            background-color: #f2f4f9;\n" +
                                        "        }\n" +
                                        "    </style>\n" +
                                        "</head>\n" +
                                        "<body bgcolor=\"#f2f4f9\">\n" +
                                        "<div style=\"display: table;margin-left: auto;margin-right: auto;\">\n" +
                                        "    <div style=\"width: 754px;background-color: #ffffff;margin-top: 40px;border-radius: 4px;box-shadow: 3px 3px 6px 0 #e3e6ec;border: solid 1px #e2e7f2; padding: 0 0 70px 0\">\n" +
                                        "        <div>\n" +
                                        "            <img style=\"margin-top: 64px;margin-left: 7.6%;margin-bottom: 66px;width: 152px;\"\n" +
                                        "                 src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        </div>\n" +
                                        "        <div style=\"font-family: 'Microsoft YaHei';\">\n" +
                                        "            <div style=\"margin-left: 7.3%;font-size: 16px\">\n" +
                                        "                <h1 style=\"margin-top: 0;margin-bottom: 0;font-size: 18px;\">\n" +
                                        "                    ${user.name}, 您好:\n" +
                                        "                </h1>\n" +
                                        "                <p style=\"font-size: 16px; line-height: 150%\">\n" +
                                        "                    您提交的一桶金计划申请，由于下面的原因，暂时无法审核通过，请确认后重新提交。\n" +
                                        "                    <p style=\"color: #20b5ff;\" >\n" +
                                        "                        ${reject_reason}\n" +
                                        "                    </p>\n" +
                                        "                </p>\n" +
                                        "            </div>\n" +
                                        "            <div style=\"font-size: 16px\">\n" +
                                        "                <hr style=\" margin-top: 30px;margin-bottom: 0;color: #e2e7f2;border: 1px solid;width:100%;\"/>\n" +
                                        "                <p style=\" margin-left: 7.3%;margin-top: 20px;margin-bottom: 0;\">\n" +
                                        "                    如有任何疑问，欢迎联系\n" +
                                        "                    <a style=\"color: #00c4ff;text-decoration: none\" href=\"mailto:feedback@askdog.com\">feedback@askdog.com</a>\n" +
                                        "                </p>\n" +
                                        "            </div>\n" +
                                        "        </div>\n" +
                                        "    </div>\n" +
                                        "    <div style=\"margin-top: 25px;margin-left: auto;margin-right: auto;text-align: center;font-size: 10px;font-family: 'Microsoft YaHei';\">\n" +
                                        "        <img style=\"width: 152px\" src=\"http://www.askdog.com/images/logo.png\">\n" +
                                        "        <p style=\"margin-top: 12px;margin-bottom: 2px;font-size: 1.4em;line-height: 18px\">版权所有©大连鸿影软件科技</p>\n" +
                                        "        <p style=\" margin: 0;font-size: 1.4em;line-height: 18px\">Copyright©2016 ASKDOG</p>\n" +
                                        "    </div>\n" +
                                        "</div>\n" +
                                        "</body>\n" +
                                        "</html>")
                                .language(SIMPLIFIED_CHINESE)
                                .build()
                );

    }

}
