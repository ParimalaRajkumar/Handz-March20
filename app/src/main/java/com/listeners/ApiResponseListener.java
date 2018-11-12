package com.listeners;

public interface ApiResponseListener< S , E> {
 public void OnResponseReceived(S s,E e);
}
