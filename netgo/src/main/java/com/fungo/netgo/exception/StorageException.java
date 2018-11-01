package com.fungo.netgo.exception;

/**
 * @author Pinger
 * @since 18-10-24 上午9:56
 */
public class StorageException extends Exception {

    public StorageException(String detailMessage) {
        super(detailMessage);
    }


    public static StorageException NOT_AVAILABLE() {
        return new StorageException("SDCard isn't available, please check SD card and permission: WRITE_EXTERNAL_STORAGE, and you must pay attention to Android6.0 RunTime Permissions!");
    }

}
