package com.askdog.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "ad_product_attribute ")
public class ProductAttribute extends Base implements Serializable {

    private static final long serialVersionUID = -6936432590749980367L;

    private Long productId;
    private List<ProductPicture> pictures;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<ProductPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<ProductPicture> pictures) {
        this.pictures = pictures;
    }

    public static class ProductPicture {

        private String name;
        private Long linkId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getLinkId() {
            return linkId;
        }

        public void setLinkId(Long linkId) {
            this.linkId = linkId;
        }
    }
}
