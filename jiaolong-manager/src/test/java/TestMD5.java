import com.test.utils.MD5;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/19 10:02
 */
public class TestMD5 {
    public static void main(String[] args) {
        String password = MD5.encryptPassword("200" , "lcg");
        System.out.println(password);
    }
}
