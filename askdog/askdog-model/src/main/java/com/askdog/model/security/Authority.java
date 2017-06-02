package com.askdog.model.security;

public interface Authority {

    String authority();

    enum Role implements Authority {

        ADMIN, USER, STORE_ADMIN, STORE_EMPLOYEE, AGENT_ADMIN, AGENT_EMPLOYEE;

        @Override
        public String authority() {
            return "ROLE_" + this.name();
        }
    }

}

