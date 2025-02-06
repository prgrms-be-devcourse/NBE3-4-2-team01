package com.ll.hotel.standard.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Empty implements Serializable {
}
