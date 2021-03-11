package com.limonnana.skate.web.rest.errors;

public class PhoneAlreadyUsedException extends BadRequestAlertException{

    private static final long serialVersionUID = 1L;

    public PhoneAlreadyUsedException() {
        super(ErrorConstants.PHONE_ALREADY_USED_TYPE, "Phone already used!", "userManagement", "userexists");
    }
}
