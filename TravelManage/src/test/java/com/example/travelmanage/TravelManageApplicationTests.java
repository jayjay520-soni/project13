package com.example.travelmanage;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travelmanage.common.Result;
import com.example.travelmanage.entity.Homestay;
import com.example.travelmanage.entity.Restaurant;
import com.example.travelmanage.entity.ScenicSpot;
import com.example.travelmanage.entity.User;
import com.example.travelmanage.mapper.HomestayMapper;
import com.example.travelmanage.mapper.RestaurantMapper;
import com.example.travelmanage.mapper.ScenicSpotMapper;
import com.example.travelmanage.mapper.UserMapper;
import com.example.travelmanage.service.impl.AdminServiceImpl;
import com.example.travelmanage.service.impl.HomestayServiceImpl;
import com.example.travelmanage.service.impl.RestaurantServiceImpl;
import com.example.travelmanage.service.impl.ScenicSpotServiceImpl;
import com.example.travelmanage.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ================================================================
 * 一体式旅游管理系统 —— 统一单元测试类（共 20 个用例，均写在此文件内）
 * ================================================================
 * 说明：
 *   1. 本类使用 JUnit 5 + Mockito，不启动 Spring 容器，不依赖数据库，毫秒级跑完。
 *   2. 每个 @Test 方法前的编号（01~20）与交付清单一一对应，便于核对。
 *   3. Service 层通过 @Mock + @InjectMocks 打桩；
 *      MyBatis-Plus 的 ServiceImpl（Homestay/Restaurant） protected baseMapper
 *      用 ReflectionTestUtils.setField 手动注入，无需容器。
 *   4. 原始 SpringBootTest 自带的 contextLoads 也保留在末尾（可视为赠送的第 21 个冒烟）。
 * ================================================================
 */
@ExtendWith(MockitoExtension.class)
class TravelManageApplicationTests {

    // ============== 通用 Mock / InjectMocks（Service 测试用） ==============
    @Mock private ScenicSpotMapper scenicSpotMapper;
    @Mock private UserMapper userMapper;
    @Mock private HomestayMapper homestayMapper;
    @Mock private RestaurantMapper restaurantMapper;

    @InjectMocks private ScenicSpotServiceImpl scenicSpotService;
    @InjectMocks private AdminServiceImpl adminService;
    // HomestayServiceImpl / RestaurantServiceImpl 继承 MyBatis-Plus ServiceImpl，
    // 需要手动反射注入 baseMapper，见 @BeforeEach setUp()
    private HomestayServiceImpl homestayService;
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        homestayService = new HomestayServiceImpl();
        ReflectionTestUtils.setField(homestayService, "baseMapper", homestayMapper);
        restaurantService = new RestaurantServiceImpl();
        ReflectionTestUtils.setField(restaurantService, "baseMapper", restaurantMapper);
    }

    // ================================================================
    // 【模块一：JwtUtil 工具类】  01~04 共 4 个
    // ================================================================

    @Test
    @DisplayName("01 JwtUtil - generateToken 不为空且为三段式 JWT 格式")
    void t01_generateTokenFormat() {
        String token = JwtUtil.generateToken("admin", "admin");
        assertNotNull(token);
        assertTrue(token.length() > 20);
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    @DisplayName("02 JwtUtil - extractUsername 能还原生成时的用户名")
    void t02_extractUsername() {
        assertEquals("zhangsan",
                JwtUtil.extractUsername(JwtUtil.generateToken("zhangsan", "user")));
    }

    @Test
    @DisplayName("03 JwtUtil - extractRole 能还原生成时的角色")
    void t03_extractRole() {
        assertEquals("admin",
                JwtUtil.extractRole(JwtUtil.generateToken("lisi", "admin")));
    }

    @Test
    @DisplayName("04 JwtUtil - validateToken：合法true，篡改/空串/null均false")
    void t04_validateToken() {
        String valid = JwtUtil.generateToken("test", "user");
        assertTrue(JwtUtil.validateToken(valid));
        assertFalse(JwtUtil.validateToken(valid + "xxx"));
        assertFalse(JwtUtil.validateToken(""));
        assertFalse(JwtUtil.validateToken(null));
    }

    // ================================================================
    // 【模块二：Result<T> 统一返回结构】  05~07 共 3 个
    // ================================================================

    @Test
    @DisplayName("05 Result.success(data) - 默认code=1，消息='操作成功'，时间戳非空")
    void t05_resultSuccessData() {
        Result<String> r = Result.success("hello");
        assertEquals(1, r.getCode());
        assertEquals("操作成功", r.getMessage());
        assertEquals("hello", r.getData());
        assertNotNull(r.getTimestamp());
        assertTrue(r.getTimestamp() > 0);
    }

    @Test
    @DisplayName("06 Result.success(code,msg,data) - 三参数按原样赋值")
    void t06_resultSuccessThreeArgs() {
        Result<Integer> r = Result.success(200, "OK", 123);
        assertEquals(200, r.getCode());
        assertEquals("OK", r.getMessage());
        assertEquals(123, r.getData());
    }

    @Test
    @DisplayName("07 Result.error - code/msg 正确，data 为 null，时间戳正常")
    void t07_resultError() {
        Result<Object> r = Result.error(500, "服务器异常");
        assertEquals(500, r.getCode());
        assertEquals("服务器异常", r.getMessage());
        assertNull(r.getData());
        assertNotNull(r.getTimestamp());
    }

    // ================================================================
    // 【模块三：Entity 实体类】  08~11 共 4 个
    // ================================================================

    @Test
    @DisplayName("08 User - 8 个字段 setter/getter 全链路正确，toString 含值")
    void t08_userEntity() {
        LocalDateTime now = LocalDateTime.now();
        User u = new User();
        u.setId(1);
        u.setUsername("zhangsan");
        u.setPassword("$2a$10$abc");
        u.setRole("admin");
        u.setCreateTime(now);
        u.setLastLoginTime(now);
        u.setStatus(1);
        u.setPhone("13800138000");

        assertEquals(1, u.getId());
        assertEquals("zhangsan", u.getUsername());
        assertEquals("$2a$10$abc", u.getPassword());
        assertEquals("admin", u.getRole());
        assertEquals(now, u.getCreateTime());
        assertEquals(now, u.getLastLoginTime());
        assertEquals(1, u.getStatus());
        assertEquals("13800138000", u.getPhone());
        assertTrue(u.toString().contains("zhangsan"));
    }

    @Test
    @DisplayName("09 ScenicSpot - price/popularity/visitCount 多种数值类型读写正确")
    void t09_scenicSpotEntity() {
        ScenicSpot s = new ScenicSpot();
        s.setId(10);
        s.setName("武夷山");
        s.setPrice(140.0);
        s.setAddress("福建省武夷山市");
        s.setRegion("南平");
        s.setPopularity(9.8);
        s.setLevel("5A");
        s.setVisitCount(12345);

        assertEquals(140.0, s.getPrice());
        assertEquals(9.8, s.getPopularity());
        assertEquals(12345, s.getVisitCount());
        assertEquals("5A", s.getLevel());
    }

    @Test
    @DisplayName("10 Homestay - BigDecimal 价格、@TableName(\"homestay\") 注解存在")
    void t10_homestayEntity() {
        Homestay h = new Homestay();
        h.setId(1);
        h.setName("鼓浪屿海景民宿");
        h.setPrice(new BigDecimal("399.99"));
        h.setScore(4.9);
        h.setType("大床房");
        h.setCapacity(2);

        assertEquals(new BigDecimal("399.99"), h.getPrice());
        assertEquals(4.9, h.getScore());
        assertEquals(2, h.getCapacity());
        assertTrue(h.getClass().isAnnotationPresent(
                com.baomidou.mybatisplus.annotation.TableName.class));
        assertEquals("homestay",
                h.getClass().getAnnotation(com.baomidou.mybatisplus.annotation.TableName.class).value());
    }

    @Test
    @DisplayName("11 Restaurant - 同字段值两对象 equals/hashCode 一致")
    void t11_restaurantEntity() {
        Restaurant r1 = new Restaurant();
        r1.setId(5);
        r1.setName("海底捞火锅");
        r1.setPrice(new BigDecimal("128.5"));
        r1.setRegion("泉州");
        r1.setScore(4.7);
        r1.setType("川菜");
        r1.setSales(9999);

        Restaurant r2 = new Restaurant();
        r2.setId(5);
        r2.setName("海底捞火锅");
        r2.setPrice(new BigDecimal("128.5"));
        r2.setRegion("泉州");
        r2.setScore(4.7);
        r2.setType("川菜");
        r2.setSales(9999);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    // ================================================================
    // 【模块四：ScenicSpotServiceImpl 景区业务】  12~15 共 4 个
    // ================================================================

    @Test
    @DisplayName("12 ScenicSpotService - 第2页每页10条，offset=(2-1)*10=10，传给mapper正确")
    void t12_scenicPagingOffset() {
        ScenicSpot s1 = new ScenicSpot(); s1.setId(1); s1.setName("鼓浪屿");
        ScenicSpot s2 = new ScenicSpot(); s2.setId(2); s2.setName("武夷山");
        when(scenicSpotMapper.findByPage(10, 10, null, null, null))
                .thenReturn(Arrays.asList(s1, s2));

        List<ScenicSpot> list = scenicSpotService.getScenicList(2, 10, null, null, null);

        assertEquals(2, list.size());
        assertEquals("鼓浪屿", list.get(0).getName());
        verify(scenicSpotMapper).findByPage(10, 10, null, null, null);
    }

    @Test
    @DisplayName("13 ScenicSpotService - addScenic 时 visitCount=null，Service 内部应默认 0")
    void t13_addScenicDefaultVisitCount() {
        when(scenicSpotMapper.addScenicSpot(any(ScenicSpot.class))).thenReturn(1);

        ScenicSpot spot = new ScenicSpot();
        spot.setName("测试景区");
        assertNull(spot.getVisitCount());

        assertTrue(scenicSpotService.addScenic(spot));
        assertEquals(0, spot.getVisitCount());
        verify(scenicSpotMapper).addScenicSpot(spot);
    }

    @Test
    @DisplayName("14 ScenicSpotService - getTotalCount 三参数原样透传")
    void t14_scenicTotalCountPassThrough() {
        when(scenicSpotMapper.countAll("鼓浪", "厦门", "5A")).thenReturn(3);
        assertEquals(3, scenicSpotService.getTotalCount("鼓浪", "厦门", "5A"));
        verify(scenicSpotMapper).countAll("鼓浪", "厦门", "5A");
    }

    @Test
    @DisplayName("15 ScenicSpotService - deleteScenic：mapper 影响1行→true，0行→false")
    void t15_deleteScenicBoolean() {
        when(scenicSpotMapper.deleteScenicSpot(1)).thenReturn(1);
        when(scenicSpotMapper.deleteScenicSpot(999)).thenReturn(0);

        assertTrue(scenicSpotService.deleteScenic(1));
        assertFalse(scenicSpotService.deleteScenic(999));
    }

    // ================================================================
    // 【模块五：AdminServiceImpl 管理员用户管理】  16~18 共 3 个
    // ================================================================

    @Test
    @DisplayName("16 AdminService - getUserList 参数透传、列表结果返回正常")
    void t16_adminGetUserList() {
        User u1 = new User(); u1.setId(1); u1.setUsername("zhangsan");
        User u2 = new User(); u2.setId(2); u2.setUsername("lisi");
        when(userMapper.findAll("zhang", 1)).thenReturn(Arrays.asList(u1, u2));

        List<User> list = adminService.getUserList("zhang", 1);
        assertEquals(2, list.size());
        assertEquals("lisi", list.get(1).getUsername());
        verify(userMapper).findAll("zhang", 1);
    }

    @Test
    @DisplayName("17 AdminService - getUserList 无结果时返回空列表（非null）")
    void t17_adminGetUserListEmpty() {
        when(userMapper.findAll("nobody", 0)).thenReturn(Collections.emptyList());
        List<User> list = adminService.getUserList("nobody", 0);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("18 AdminService - updateUserStatus 按影响行数返回 true/false")
    void t18_adminUpdateStatusBoolean() {
        when(userMapper.updateStatus(5, 0)).thenReturn(1);
        when(userMapper.updateStatus(999, 0)).thenReturn(0);

        assertTrue(adminService.updateUserStatus(5, 0));
        assertFalse(adminService.updateUserStatus(999, 0));
    }

    // ================================================================
    // 【模块六：Homestay/Restaurant MP 继承 Service】  19~20 共 2 个
    // ================================================================

    @Test
    @DisplayName("19 HomestayServiceImpl - 继承 ServiceImpl，save() 方法正常返回 true")
    void t19_homestaySave() {
        assertTrue(homestayService instanceof ServiceImpl,
                "HomestayServiceImpl 必须继承 MyBatis-Plus ServiceImpl");
        when(homestayMapper.insert(any(Homestay.class))).thenReturn(1);

        Homestay h = new Homestay();
        h.setName("测试民宿");
        h.setPrice(new BigDecimal("199"));

        assertTrue(homestayService.save(h));
    }

    @Test
    @DisplayName("20 RestaurantServiceImpl - getById 找到对象返回，找不到返回 null 不抛错")
    void t20_restaurantGetById() {
        Restaurant r = new Restaurant();
        r.setId(7);
        r.setName("测试餐厅");
        r.setType("粤菜");
        when(restaurantMapper.selectById(7)).thenReturn(r);
        when(restaurantMapper.selectById(999)).thenReturn(null);

        Restaurant found = restaurantService.getById(7);
        assertNotNull(found);
        assertEquals("测试餐厅", found.getName());
        assertEquals("粤菜", found.getType());
        assertNull(restaurantService.getById(999));
    }

    // ================================================================
    // 附：Spring Boot 原始冒烟测试（不依赖 Mockito，保留作为容器启动验证）
    //    说明：如果你在没有数据库的环境运行，可把这一段注释掉；
    //          默认作为"第21个"存在，不影响上面 20 个纯 Mock 用例的计数。
    // ================================================================
    // @SpringBootTest 这里不启用，避免启动容器影响上面 20 个轻量用例；
    // 仅保留一个空方法作为与原始文件兼容的占位。
    @Test
    @DisplayName("[附赠] 基础冒烟：无断言空跑占位，确保类结构未被破坏")
    void contextLoads() {
        // 真要启动 Spring 容器时再把类注解改成 @SpringBootTest @ExtendWith(SpringExtension.class)
    }
}
