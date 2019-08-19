import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/16 10:19
 */
public class TestRedis {

    @Resource
    private RedisTemplate redisTemplate;
    @org.junit.Test
    public void redisLoginDate(){

        String ym = new SimpleDateFormat("yyyy-MM").format(new Date());//-年-月
        System.out.println("ym"+ym);
        String ymd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        System.out.println(ymd);
        Boolean aBoolean = redisTemplate.opsForHash().hasKey(ym,ymd);
        System.out.println(aBoolean);
        if(redisTemplate.opsForHash().hasKey(ym,ymd)){
            redisTemplate.opsForHash().increment(ym,ymd,1);
        }else{
            redisTemplate.opsForHash().put(ym,ymd,"1");
        }
    }
}
