# FakePlayer Plugin - Minecraft Paper 1.18.2

Plugin để tạo fake player trên server Minecraft Paper 1.18.2 với các tính năng tối ưu hiệu năng.

## ✨ Tính Năng Chính

✅ **Hiển thị số lượng fake player ở Tab List** - Khi người chơi nhấn TAB, sẽ thấy số lượng fake player và real player
✅ **Hiển thị ở quản lý server** - Hiển thị trong header/footer của tab list
✅ **Tự động random username** - Tên được thay đổi tự động sau khoảng thời gian cấu hình
✅ **Tên giống người thật** - Sử dụng tên thực tế (FirstName + LastName, Prefix + Suffix, v.v.)
✅ **Không gây lag** - Sử dụng async tasks và tối ưu hiệu năng

## 📦 Cài Đặt

1. **Build plugin:**
```bash
mvn clean package
```

2. **Copy JAR file vào plugins folder:**
```bash
cp target/FakePlayer-1.0.0.jar /path/to/server/plugins/
```

3. **Restart server**

## 🎮 Lệnh Sử Dụng

```
/fakeplayer add <count>      - Thêm fake player (ví dụ: /fakeplayer add 10)
/fakeplayer remove <name>    - Xóa fake player cụ thể
/fakeplayer list             - Liệt kê tất cả fake player
/fakeplayer clear            - Xóa tất cả fake player
/fakeplayer reload           - Reload config
/fp <command>                - Alias ngắn
```

## ⚙️ Cấu Hình

File `config.yml`:

```yaml
# Số lượng fake player tối đa
max-fake-players: 50

# Khoảng thời gian rotate tên (giây)
rotate-interval: 300
```

## 🔧 Cách Hoạt Động

1. **Tab List Display**: Khi người chơi nhấn TAB, header/footer sẽ hiển thị:
   - Số lượng real player
   - Số lượng fake player
   - Tổng số player

2. **Name Generation**: Plugin tạo tên ngẫu nhiên theo 3 cách:
   - FirstName + LastName (ví dụ: "AlexSmith")
   - Prefix + Suffix (ví dụ: "ProMaster")
   - FirstName + Number (ví dụ: "John1234")

3. **Auto Rotation**: Mỗi 5 phút (có thể cấu hình), tất cả fake player sẽ được gán tên mới

4. **Performance**:
   - Sử dụng synchronized Set để tránh race condition
   - Async tasks để không block main thread
   - Tối ưu memory usage