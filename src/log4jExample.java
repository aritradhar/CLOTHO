import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;


//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar																* *
//MT12004																			* *
//M.TECH CSE																		* * 
//INFORMATION SECURITY																* *
//IIIT-Delhi																		* *	 
//---------------------------------------------------------------------------------	* *																				* *
/////////////////////////////////////////////////									* *
//The program will do the following::::         //									* *
/////////////////////////////////////////////////									* *
//version 1.0																		* *
//*********************************************************************************** * 
//*************************************************************************************
import org.apache.log4j.or.ObjectRenderer;


import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

class CustomerOrder {

  /** Holds value of property productName. */
  private String productName;

  /** Holds value of property productCode. */
  private int productCode;

  /** Holds value of property productPrice. */
  private int productPrice;

  /** Creates a new instance of CustomerOrder */
  public CustomerOrder() {
  }

  public CustomerOrder(String name, int code, int price) {
    this.productCode = code;
    this.productPrice = price;
    this.productName = name;
  }

  /**
   * Getter for property productName.
   * 
   * @return Value of property productName.
   */
  public String getProductName() {
    return this.productName;
  }

  /**
   * Setter for property productName.
   * 
   * @param productName
   *            New value of property productName.
   */
  public void setProductName(String productName) {
    this.productName = productName;
  }

  /**
   * Getter for property productCode.
   * 
   * @return Value of property productCode.
   */
  public int getProductCode() {
    return this.productCode;
  }

  /**
   * Setter for property productCode.
   * 
   * @param productCode
   *            New value of property productCode.
   */
  public void setProductCode(int productCode) {
    this.productCode = productCode;
  }

  /**
   * Getter for property productPrice.
   * 
   * @return Value of property productPrice.
   */
  public int getProductPrice() {
    return this.productPrice;
  }

  /**
   * Setter for property productPrice.
   * 
   * @param productPrice
   *            New value of property productPrice.
   */
  public void setProductPrice(int productPrice) {
    this.productPrice = productPrice;
  }
}

class ProductFilter extends Filter {
  /** Creates a new instance of ProductFilter */
  public ProductFilter() {
  }

  public int decide(LoggingEvent event) {
    int result = this.ACCEPT;
    //obtaining the message object passed through Logger
    Object message = event.getMessage();
    //checking if the message object is of correct type
    if (message instanceof CustomerOrder) {
      CustomerOrder order = (CustomerOrder) message;
      int productCode = order.getProductCode();
      //checking for the product code greater than 100 only
      if (productCode < 100) {
        result = this.DENY;
      }
    } else {
      //this filter can ignore this, pass to next filter
      result = this.NEUTRAL;
    }

    return result;
  }
}

class OrderRenderer implements ObjectRenderer {
  private static final String separator = "-";

  /** Creates a new instance of OrderRenderer */
  public OrderRenderer() {
  }

  public String doRender(Object obj) {
    StringBuffer buffer = new StringBuffer(50);
    CustomerOrder order = null;
    String productName = null;
    int productCode = 0;
    int productPrice = 0;
    //check if the instance is of correct type CustomerOrder
    if (obj instanceof CustomerOrder) {
      order = (CustomerOrder) obj;
      productName = order.getProductName();
      productCode = order.getProductCode();
      productPrice = order.getProductPrice();

      buffer.append(productName);
      buffer.append(separator);
      buffer.append(new Integer(productCode).toString());
      buffer.append(separator);
      buffer.append(new Integer(productPrice).toString());
    }

    return buffer.toString();
  }
}

public class log4jExample {
  private static Logger logger = Logger.getLogger("name");

  /** Creates a new instance of ProductFilterDemo */
  public log4jExample() {
  }

  public void processOrder(CustomerOrder order) {
    logger.info(order);
  }

  public static void main(String args[]) {
    CustomerOrder order1 = new CustomerOrder("Beer", 101, 20);
    CustomerOrder order2 = new CustomerOrder("Lemonade", 95, 10);
    CustomerOrder order3 = new CustomerOrder("Chocolate", 223, 5);

    log4jExample demo = new log4jExample();
    demo.processOrder(order1);
    demo.processOrder(order2);
    demo.processOrder(order3);
  }
}
//filter_properties.xml
/*
 * 
 * <?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE log4j:configuration SYSTEM
 * "log4j.dtd">
 * 
 * <log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
 * 
 * <renderer renderedClass="com.apress.business.CustomerOrder"
 * renderingClass="com.apress.logging.log4j.renderer.OrderRenderer"> </renderer>
 * 
 * <appender name="A1" class="org.apache.log4j.ConsoleAppender">
 * 
 * <layout class="org.apache.log4j.PatternLayout"> <param
 * name="ConversionPattern" value="%t %-5p %c{2} - %m%n"/> </layout> <filter
 * class="com.apress.logging.log4j.filter.ProductFilter"/> </appender>
 * 
 * <logger name="com.apress.logging.log4j"> <level value="debug"/> <appender-ref
 * ref="A1"/> </logger> </log4j:configuration>
 */