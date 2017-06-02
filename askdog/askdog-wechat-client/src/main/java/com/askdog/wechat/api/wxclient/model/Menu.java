package com.askdog.wechat.api.wxclient.model;

import com.askdog.wechat.api.WxObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public class Menu implements WxObject {

    @JsonProperty(value = "button")
    private List<Button> buttons;

    public static class Button {

        private ButtonType type;
        private String name;
        private String key;
        private String url;

        @JsonProperty(value = "sub_button")
        private List<Button> subButtons;

        public ButtonType getType() {
            return type;
        }

        public void setType(ButtonType type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<Button> getSubButtons() {
            return subButtons;
        }

        public void setSubButtons(List<Button> subButtons) {
            this.subButtons = subButtons;
        }
    }

    public enum ButtonType {

        CLICK("click"),
        VIEW("view"),
        SCANCODE_PUSH("scancode_push"),
        SCANCODE_WAITMSG("scancode_waitmsg"),
        PIC_SYSPHOTO("pic_sysphoto"),
        PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
        PIC_WEIXIN("pic_weixin"),
        LOCATION_SELECT("location_select"),
        MEDIA_ID("media_id"),
        VIEW_LIMITED("view_limited");

        private String value;

        ButtonType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static ButtonType fromValue(String value) {
            for (ButtonType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }

            throw new IllegalArgumentException("not found ButtonType enum value for " + value);
        }

    }
}
