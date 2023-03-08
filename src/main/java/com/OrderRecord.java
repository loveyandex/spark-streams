package com;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRecord {

      private String symbol;

      private String price;
      private String amount;
      
      private String side;
      private String type;

}
