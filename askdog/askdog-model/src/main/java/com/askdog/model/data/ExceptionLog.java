package com.askdog.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "mc_exception_log")
public class ExceptionLog extends Base implements Serializable {

    private static final long serialVersionUID = 496405969351916532L;

    private Date occurenceTime;
    private String machineId;
    private String message;
    private String stackInfo;

    public Date getOccurenceTime() {
        return occurenceTime;
    }

    public void setOccurenceTime(Date occurenceTime) {
        this.occurenceTime = occurenceTime;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackInfo() {
        return stackInfo;
    }

    public void setStackInfo(String stackInfo) {
        this.stackInfo = stackInfo;
    }
}
