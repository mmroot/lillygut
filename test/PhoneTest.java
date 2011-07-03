import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class PhoneTest extends FunctionalTest {

    @Test
    public void testThatPhonePageWorks() {
        Response response = GET("/phone");
        
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

    @Test
    public void testThatThereIsThePhoneField() {
        Response response = GET("/phone");
        System.out.println(response.out);
        assertContentMatch("//label[1]", response);
        assertContentMatch("//input[@name='phone']", response);
    }
    
}