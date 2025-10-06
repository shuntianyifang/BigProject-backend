package org.bigseven.util;

import jakarta.annotation.Resource;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.entity.User;
import org.bigseven.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 超级管理员账号管理控制台
 * 用于在服务器控制台执行超级管理员账号的增删改查操作
 * 该功能只能在服务器端通过命令行使用，不能通过前端API访问
 */
@Component
public class SuperAdminManagementConsole implements CommandLineRunner {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    // 添加标志位，用于控制后台监听线程
    private volatile boolean consoleActive = true;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否有特定系统属性来触发超级管理员管理功能
        String manageSuperAdmin = System.getProperty("manage.super.admin");
        if ("true".equals(manageSuperAdmin)) {
            try (Scanner scanner = new Scanner(System.in)) {
                startSuperAdminManagement(scanner);
            }
            System.exit(0); // 执行完管理操作后退出程序
        }

        // 启动后台监听线程，监听文件触发
        startFileTriggerListener();
        // 默认不自动启动管理界面，避免影响正常服务启动
    }

    /**
     * 启动文件触发监听线程
     */
    private void startFileTriggerListener() {
        Thread listenerThread = new Thread(() -> {
            System.out.println("超级管理员管理功能已启用，创建文件 /tmp/super_admin_manage 即可启动管理界面");
            File triggerFile = new File("/tmp/super_admin_manage");

            try {
                while (consoleActive) {
                    if (triggerFile.exists()) {
                        // 删除触发文件
                        triggerFile.delete();

                        synchronized (this) {
                            System.out.println("\n=== 检测到超级管理员管理请求 ===");
                            // 直接在当前线程中运行管理功能
                            runInteractiveSuperAdminManagement();
                            System.out.println("超级管理员管理操作已完成");
                            System.out.println("提示：创建文件 /tmp/start_super_admin_manage 可再次启动管理界面");
                        }
                    }

                    // 每秒检查一次
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.err.println("文件监听线程发生错误: " + e.getMessage());
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /**
     * 运行交互式超级管理员管理功能
     */
    private void runInteractiveSuperAdminManagement() {
        try {
            // 创建一个新的Scanner实例，确保使用的是标准输入
            Scanner scanner = new Scanner(System.in);
            startSuperAdminManagement(scanner);
        } catch (Exception e) {
            System.err.println("运行超级管理员管理功能时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 启动超级管理员管理控制台
     */
    public void startSuperAdminManagement(Scanner scanner) {
        System.out.println("=== 超级管理员账号管理控制台 ===");
        System.out.println("注意：此功能只能在服务器端使用，不能通过前端API访问");
        System.out.println();

        while (true) {
            printMenu();
            String choice = "";
            try {
                // 使用更安全的方式读取输入
                if (scanner.hasNext()) {
                    choice = scanner.next().trim();
                    // 消费掉剩余的换行符
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                } else {
                    System.out.println("未检测到有效输入，自动退出管理界面");
                    return;
                }
            } catch (Exception e) {
                System.out.println("读取输入时出错: " + e.getMessage() + "，自动退出管理界面");
                return;
            }
            switch (choice) {
                case "1":
                    createSuperAdmin(scanner);
                    break;
                case "2":
                    listSuperAdmins();
                    break;
                case "3":
                    updateSuperAdmin(scanner);
                    break;
                case "4":
                    deleteSuperAdmin(scanner);
                    break;
                case "5":
                    System.out.println("退出超级管理员管理控制台");
                    return;
                default:
                    System.out.println("无效选择，请重新输入");
            }

            System.out.println("\n按回车键继续...");
            try {
                // 等待用户按回车键继续
                scanner.nextLine();
            } catch (Exception e) {
                // 忽略继续输入的异常
            }
        }
    }

    /**
     * 打印菜单选项
     */
    private void printMenu() {
        System.out.println("===== 菜单 =====");
        System.out.println("1. 创建超级管理员账号");
        System.out.println("2. 列出所有超级管理员账号");
        System.out.println("3. 更新超级管理员账号");
        System.out.println("4. 删除超级管理员账号");
        System.out.println("5. 退出");
        System.out.println("================");
    }

    /**
     * 创建超级管理员账号
     */
    private void createSuperAdmin(Scanner scanner) {
        try {
            System.out.println("=== 创建超级管理员账号 ===");
            
            System.out.print("请输入用户名: ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("用户名不能为空");
                return;
            }
            
            // 检查用户名是否已存在
            if (userMapper.selectByUsername(username) != null) {
                System.out.println("用户名已存在");
                return;
            }

            System.out.print("请输入昵称: ");
            String nickname = scanner.nextLine().trim();
            if (nickname.isEmpty()) {
                System.out.println("昵称不能为空");
                return;
            }

            System.out.print("请输入邮箱: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("邮箱不能为空");
                return;
            }
            
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("邮箱格式不正确");
                return;
            }

            System.out.print("请输入密码: ");
            String password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("密码不能为空");
                return;
            }
            
            if (password.length() < 6) {
                System.out.println("密码长度不能少于6位");
                return;
            }

            // 创建超级管理员用户
            User user = User.builder()
                    .username(username)
                    .nickname(nickname)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .userType(UserTypeEnum.SUPER_ADMIN)
                    .deleted(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .lastLoginAt(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
            System.out.println("超级管理员账号创建成功，用户ID: " + user.getUserId());
        } catch (Exception e) {
            System.out.println("创建超级管理员账号失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 列出所有超级管理员账号
     */
    private void listSuperAdmins() {
        try {
            System.out.println("=== 超级管理员账号列表 ===");
            List<User> superAdmins = userMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                            .eq(User::getUserType, UserTypeEnum.SUPER_ADMIN)
                            .eq(User::getDeleted, false)
            );

            if (superAdmins.isEmpty()) {
                System.out.println("暂无超级管理员账号");
                return;
            }

            System.out.printf("%-6s %-15s %-15s %-25s %-20s%n", "用户ID", "用户名", "昵称", "邮箱", "创建时间");
            System.out.println("------------------------------------------------------------------------");
            for (User user : superAdmins) {
                System.out.printf("%-6d %-15s %-15s %-25s %-20s%n",
                        user.getUserId(),
                        user.getUsername(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getCreatedAt().toString());
            }
        } catch (Exception e) {
            System.out.println("查询超级管理员账号列表失败: " + e.getMessage());

        }
    }

    /**
     * 更新超级管理员账号
     */
    private void updateSuperAdmin(Scanner scanner) {
        try {
            System.out.println("=== 更新超级管理员账号 ===");
            
            System.out.print("请输入要更新的超级管理员用户ID: ");
            String userIdStr = scanner.nextLine().trim();
            if (userIdStr.isEmpty()) {
                System.out.println("用户ID不能为空");
                return;
            }

            Integer userId = Integer.parseInt(userIdStr);
            User user = userMapper.selectById(userId);
            if (user == null) {
                System.out.println("用户不存在");
                return;
            }

            if (user.getUserType() != UserTypeEnum.SUPER_ADMIN) {
                System.out.println("该用户不是超级管理员");
                return;
            }

            System.out.print("请输入新的昵称 (当前: " + user.getNickname() + ", 直接回车跳过): ");
            String nickname = scanner.nextLine().trim();
            if (!nickname.isEmpty()) {
                user.setNickname(nickname);
            }

            System.out.print("请输入新的邮箱 (当前: " + user.getEmail() + ", 直接回车跳过): ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    System.out.println("邮箱格式不正确，邮箱未更新");
                } else {
                    user.setEmail(email);
                }
            }

            System.out.print("是否需要重置密码? (y/N): ");
            String resetPassword = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(resetPassword)) {
                System.out.print("请输入新密码: ");
                String newPassword = scanner.nextLine().trim();
                if (newPassword.isEmpty()) {
                    System.out.println("密码不能为空，密码未更新");
                } else if (newPassword.length() < 6) {
                    System.out.println("密码长度不能少于6位，密码未更新");
                } else {
                    user.setPassword(passwordEncoder.encode(newPassword));
                }
            }

            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);
            System.out.println("超级管理员账号更新成功");
        } catch (NumberFormatException e) {
            System.out.println("用户ID格式错误");
        } catch (Exception e) {
            System.out.println("更新超级管理员账号失败: " + e.getMessage());

        }
    }

    /**
     * 删除超级管理员账号
     */
    private void deleteSuperAdmin(Scanner scanner) {
        try {
            System.out.println("=== 删除超级管理员账号 ===");
            
            System.out.print("请输入要删除的超级管理员用户ID: ");
            String userIdStr = scanner.nextLine().trim();
            if (userIdStr.isEmpty()) {
                System.out.println("用户ID不能为空");
                return;
            }

            Integer userId = Integer.parseInt(userIdStr);
            User user = userMapper.selectById(userId);
            if (user == null) {
                System.out.println("用户不存在");
                return;
            }

            if (user.getUserType() != UserTypeEnum.SUPER_ADMIN) {
                System.out.println("该用户不是超级管理员");
                return;
            }

            // 检查是否是最后一个超级管理员
            synchronized (this) {
                List<User> superAdmins = userMapper.selectList(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                                .eq(User::getUserType, UserTypeEnum.SUPER_ADMIN)
                                .eq(User::getDeleted, false)
                );

                if (superAdmins.size() <= 1) {
                    System.out.println("不能删除最后一个超级管理员账号");
                    return;
                }

                System.out.print("确认删除超级管理员 " + user.getUsername() + " ? (y/N): ");
                String confirm = scanner.nextLine().trim();
                if ("y".equalsIgnoreCase(confirm)) {
                    user.setDeleted(true);
                    userMapper.updateById(user);
                    System.out.println("超级管理员账号删除成功");
                } else {
                    System.out.println("取消删除操作");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("用户ID格式错误");
        } catch (Exception e) {
            System.out.println("删除超级管理员账号失败: " + e.getMessage());

        }
    }
}
