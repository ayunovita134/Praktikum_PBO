import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class MenuItem {
    private static int penghitungId = 1;
    private final String id;
    private String nama;
    private String kategori;
    private double harga;
    private boolean tersedia;

    public MenuItem(String nama, String kategori, double harga) {
        this.id = String.format("%02d", penghitungId++);
        setNama(nama);
        setKategori(kategori);
        setHarga(harga);
        this.tersedia = true;
    }

    public String getId() { return id; }
    
    public String getNama() { return nama; }
    
    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        this.nama = nama.trim();
    }
    
    public String getKategori() { return kategori; }
    
    protected boolean isKategoriValid(String kategori) {
        String[] kategoriValid = {"Kopi", "Makanan", "Minuman"};
        for (String valid : kategoriValid) {
            if (valid.equalsIgnoreCase(kategori)) {
                return true;
            }
        }
        return false;
    }
    
    public void setKategori(String kategori) {
        if (!isKategoriValid(kategori)) {
            throw new IllegalArgumentException("Kategori tidak valid!");
        }
        this.kategori = kategori;
    }
    
    public double getHarga() { return harga; }
    
    public void setHarga(double harga) {
        if (harga < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif!");
        }
        this.harga = harga;
    }
    
    public boolean isTersedia() { return tersedia; }
    
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }

    @Override
    public String toString() {
        return String.format("ID: %s | %-20s | %-10s | Rp %.2f | %s", 
                id, nama, kategori, harga, tersedia ? "Tersedia" : "Tidak Tersedia");
    }
}

class OrderItem {
    private MenuItem menuItem;
    private int jumlah;

    public OrderItem(MenuItem menuItem, int jumlah) {
        this.menuItem = menuItem;
        this.jumlah = jumlah;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public double getSubtotal() { return menuItem.getHarga() * jumlah; }

    @Override
    public String toString() {
        return String.format("%-20s | %d | Rp %.2f | Rp %.2f", 
                menuItem.getNama(), jumlah, menuItem.getHarga(), getSubtotal());
    }
}

class Order {
    private static int penghitungId = 1;
    private final String id;
    private final ArrayList<OrderItem> daftarItem;
    private String nomorMeja;
    private final String namaPelanggan;
    private final LocalDateTime waktuPesan;
    private String status; 

    private List<OrderItem> items = new ArrayList<>();

    public Order(String nomorMeja, String namaPelanggan) {
        this.id = String.format("%01d", penghitungId++);
        this.daftarItem = new ArrayList<>();
        setNomorMeja(nomorMeja);
        this.namaPelanggan = namaPelanggan;
        this.waktuPesan = LocalDateTime.now();
        this.status = "Baru";
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getId() { return id; }
    
    public ArrayList<OrderItem> getDaftarItem() { 
        return new ArrayList<>(daftarItem); 
    }
    
    public String getNomorMeja() { return nomorMeja; }
    
    public void setNomorMeja(String nomorMeja) {
        if (nomorMeja == null || nomorMeja.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor meja tidak boleh kosong!");
        }
        this.nomorMeja = nomorMeja.trim();
    }
    
    public String getNamaPelanggan() { return namaPelanggan; }
    
    public LocalDateTime getWaktuPesan() { return waktuPesan; }
    
    public String getStatus() { return status; }
    
    protected boolean isStatusValid(String status) {
        String[] statusValid = {"Baru", "Diproses", "Selesai", "Dibatalkan"};
        for (String valid : statusValid) {
            if (valid.equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    public void setStatus(String status) {
        if (!isStatusValid(status)) {
            throw new IllegalArgumentException("Status pesanan tidak valid!");
        }
        this.status = status;
    }

    public void tambahItem(MenuItem menuItem, int jumlah) {
        for (OrderItem item : items) {
            if (item.getMenuItem().getId().equals(menuItem.getId())) {
                item.setJumlah(item.getJumlah() + jumlah);
                return;
            }
        }
        items.add(new OrderItem(menuItem, jumlah));
    }

    public boolean hapusItem(String menuItemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getMenuItem().getId().equals(menuItemId)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public double hitungTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("===== PESANAN #").append(id).append(" =====\n");
        sb.append("Meja: ").append(nomorMeja).append("\n");
        sb.append("Pelanggan: ").append(namaPelanggan).append("\n");
        sb.append("Waktu: ").append(waktuPesan.format(formatter)).append("\n");
        sb.append("Status: ").append(status).append("\n\n");
        
        sb.append(String.format("%-20s | %s | %-8s | %-8s\n", "Nama Item", "Qty", "Harga", "Subtotal"));
        sb.append("----------------------------------------------------------\n");
        
        for (OrderItem item : items) {
            sb.append(item.toString()).append("\n");
        }
        
        sb.append("----------------------------------------------------------\n");
        sb.append(String.format("TOTAL: Rp %.2f\n", hitungTotal()));
        
        return sb.toString();
    }
}

class User {
    private String username;
    private String password;
    private String role; 
    private String nama;

    public User(String username, String password, String role, String nama) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nama = nama;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getNama() { return nama; }
}

public class RestaurantEase {
    private static ArrayList<MenuItem> menuItems = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initSampleData();
        
        boolean isRunning = true;
        while (isRunning) {
            if (currentUser == null) {
                tampilkanMenuLogin();
                int pilihan = getIntInput("Pilih menu: ");
                
                switch (pilihan) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        isRunning = false;
                        System.out.println("Terima kasih telah menggunakan Restaurant Ease!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } else if (currentUser.getRole().equals("admin")) {
                tampilkanMenuAdmin();
                int pilihan = getIntInput("Pilih menu: ");
                
                switch (pilihan) {
                    case 1:
                        kelolaMenu();
                        break;
                    case 2:
                        adminManagePesanan();
                        break;
                    case 3:
                        logout();
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } else { 
                tampilkanMenuPelanggan();
                int pilihan = getIntInput("Pilih menu: ");
                
                switch (pilihan) {
                    case 1:
                        lihatSemuaMenu();
                        break;
                    case 2:
                        kelolaPesanan();
                        break;
                    case 3:
                        logout();
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            }
        }
        scanner.close();
    }

    private static void initSampleData() {
        menuItems.add(new MenuItem("Espresso", "Kopi", 18000));
        menuItems.add(new MenuItem("Cappuccino", "Kopi", 25000));
        menuItems.add(new MenuItem("Latte", "Kopi", 23000));
        menuItems.add(new MenuItem("Americano", "Kopi", 20000));
        menuItems.add(new MenuItem("Croissant", "Makanan", 15000));
        menuItems.add(new MenuItem("Cheesecake", "Makanan", 28000));
        menuItems.add(new MenuItem("Air Mineral", "Minuman", 8000));
        
        users.put("admin", new User("admin", "admin123", "admin", "Administrator"));
        users.put("pelanggan", new User("pelanggan", "pelanggan123", "pelanggan", "Pelanggan Umum"));
    }
    
    private static void tampilkanMenuLogin() {
        System.out.println("\n===== RESTAURANT EASE =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Keluar");
    }
    
    private static void login() {
        System.out.println("\n===== LOGIN =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login berhasil! Selamat datang, " + user.getNama() + "!");
        } else {
            System.out.println("Username atau password salah!");
        }
    }
    
    private static void register() {
        System.out.println("\n===== REGISTER =====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        if (users.containsKey(username)) {
            System.out.println("Username sudah digunakan!");
            return;
        }
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Nama: ");
        String nama = scanner.nextLine();
        
        User newUser = new User(username, password, "pelanggan", nama);
        users.put(username, newUser);
        System.out.println("Registrasi berhasil! Silakan login.");
    }
    
    private static void logout() {
        currentUser = null;
        System.out.println("Logout berhasil!");
    }
    
    private static void tampilkanMenuAdmin() {
        System.out.println("\n===== MENU ADMIN =====");
        System.out.println("1. Kelola Menu");
        System.out.println("2. Kelola Pesanan");
        System.out.println("3. Logout");
    }
    
    private static void tampilkanMenuPelanggan() {
        System.out.println("\n===== MENU PELANGGAN =====");
        System.out.println("1. Lihat Menu");
        System.out.println("2. Pesanan");
        System.out.println("3. Logout");
    }
    
    private static void adminManagePesanan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n===== KELOLA PESANAN (ADMIN) =====");
            System.out.println("1. Lihat Semua Pesanan");
            System.out.println("2. Ubah Status Pesanan");
            System.out.println("3. Kembali ke Menu Admin");
            
            int pilihan = getIntInput("Pilih menu: ");
            
            switch (pilihan) {
                case 1:
                    lihatSemuaPesanan();
                    break;
                case 2:
                    ubahStatusPesanan();
                    break;
                case 3:
                    kembali = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static int getIntInput(String prompt) {
        int input = 0;
        boolean valid = false;
        
        while (!valid) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka.");
            }
        }
        
        return input;
    }

    private static double getDoubleInput(String prompt) {
        double input = 0;
        boolean valid = false;
        
        while (!valid) {
            System.out.print(prompt);
            try {
                input = Double.parseDouble(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka.");
            }
        }
        
        return input;
    }

    private static void kelolaMenu() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n===== KELOLA MENU =====");
            System.out.println("1. Lihat Semua Menu");
            System.out.println("2. Tambah Menu Baru");
            System.out.println("3. Edit Menu");
            System.out.println("4. Hapus Menu");
            System.out.println("5. Kembali ke Menu Admin");
            
            int pilihan = getIntInput("Pilih menu: ");
            
            switch (pilihan) {
                case 1:
                    lihatSemuaMenu();
                    break;
                case 2:
                    tambahMenu();
                    break;
                case 3:
                    editMenu();
                    break;
                case 4:
                    hapusMenu();
                    break;
                case 5:
                    kembali = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void lihatSemuaMenu() {
        System.out.println("\n===== DAFTAR MENU =====");
        
        if (menuItems.isEmpty()) {
            System.out.println("Tidak ada menu tersedia.");
            return;
        }
        
        System.out.println(String.format("%-10s | %-20s | %-10s | %-10s | %s", 
                "ID", "Nama", "Kategori", "Harga", "Status"));
        System.out.println("-----------------------------------------------------------------------");
        
        for (MenuItem item : menuItems) {
            System.out.println(item);
        }
        
        if (currentUser != null && currentUser.getRole().equals("pelanggan")) {
            System.out.print("\nTekan Enter untuk kembali...");
            scanner.nextLine();
        }
    }

    private static void tambahMenu() {
        System.out.println("\n===== TAMBAH MENU BARU =====");
        
        System.out.print("Nama menu: ");
        String nama = scanner.nextLine();
        
        System.out.print("Kategori (Kopi/Makanan/Minuman): ");
        String kategori = scanner.nextLine();
        
        double harga = getDoubleInput("Harga (Rp): ");
        
        MenuItem newItem = new MenuItem(nama, kategori, harga);
        menuItems.add(newItem);
        
        System.out.println("Menu berhasil ditambahkan!");
    }

    private static void editMenu() {
        System.out.println("\n===== EDIT MENU =====");
        lihatSemuaMenu();
        
        if (menuItems.isEmpty()) {
            return;
        }
        
        System.out.print("Masukkan ID menu yang akan diedit: ");
        String id = scanner.nextLine();
        
        MenuItem itemToEdit = null;
        for (MenuItem item : menuItems) {
            if (item.getId().equals(id)) {
                itemToEdit = item;
                break;
            }
        }
        
        if (itemToEdit == null) {
            System.out.println("Menu dengan ID tersebut tidak ditemukan!");
            return;
        }
        
        System.out.println("Menu yang akan diedit: " + itemToEdit);
        
        System.out.print("Nama baru (kosongkan jika tidak ingin mengubah): ");
        String newNama = scanner.nextLine();
        if (!newNama.isEmpty()) {
            itemToEdit.setNama(newNama);
        }
        
        System.out.print("Kategori baru (kosongkan jika tidak ingin mengubah): ");
        String newKategori = scanner.nextLine();
        if (!newKategori.isEmpty()) {
            itemToEdit.setKategori(newKategori);
        }
        
        System.out.print("Ubah harga? (y/n): ");
        String ubahHarga = scanner.nextLine();
        if (ubahHarga.equalsIgnoreCase("y")) {
            double newHarga = getDoubleInput("Harga baru (Rp): ");
            itemToEdit.setHarga(newHarga);
        }
        
        System.out.print("Ubah status ketersediaan? (y/n): ");
        String ubahStatus = scanner.nextLine();
        if (ubahStatus.equalsIgnoreCase("y")) {
            System.out.print("Status baru (1: Tersedia, 0: Tidak Tersedia): ");
            int status = Integer.parseInt(scanner.nextLine());
            itemToEdit.setTersedia(status == 1);
        }
        
        System.out.println("Menu berhasil diperbarui!");
    }

    private static void hapusMenu() {
        System.out.println("\n===== HAPUS MENU =====");
        lihatSemuaMenu();
        
        if (menuItems.isEmpty()) {
            return;
        }
        
        System.out.print("Masukkan ID menu yang akan dihapus: ");
        String id = scanner.nextLine();
        
        MenuItem itemToRemove = null;
        for (MenuItem item : menuItems) {
            if (item.getId().equals(id)) {
                itemToRemove = item;
                break;
            }
        }
        
        if (itemToRemove == null) {
            System.out.println("Menu dengan ID tersebut tidak ditemukan!");
            return;
        }
        
        System.out.println("Menu yang akan dihapus: " + itemToRemove);
        System.out.print("Anda yakin ingin menghapus menu ini? (y/n): ");
        String konfirmasi = scanner.nextLine();
        
        if (konfirmasi.equalsIgnoreCase("y")) {
            menuItems.remove(itemToRemove);
            System.out.println("Menu berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }
    
    private static void kelolaPesanan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n===== PESANAN =====");
            System.out.println("1. Buat Pesanan Baru");
            System.out.println("2. Lihat Pesanan Saya");
            System.out.println("3. Tambah Item ke Pesanan");
            System.out.println("4. Hapus Item dari Pesanan");
            System.out.println("5. Kembali ke Menu Pelanggan");
            
            int pilihan = getIntInput("Pilih menu: ");
            
            switch (pilihan) {
                case 1:
                    buatPesananBaru();
                    break;
                case 2:
                    lihatPesananSaya();
                    break;
                case 3:
                    tambahItemKePesanan();
                    break;
                case 4:
                    hapusItemDariPesanan();
                    break;
                case 5:
                    kembali = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void lihatPesananSaya() {
        System.out.println("\n===== PESANAN SAYA =====");
        
        if (orders.isEmpty()) {
            System.out.println("Anda belum memiliki pesanan.");
            return;
        }
        
        System.out.println(String.format("%-10s | %-15s | %-20s | %s", 
                "ID", "Meja", "Waktu", "Status"));
        System.out.println("----------------------------------------------------------------");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        boolean adaPesanan = false;
        
        for (Order order : orders) {
            if (order.getNamaPelanggan().equals(currentUser.getNama())) {
                adaPesanan = true;
                System.out.println(String.format("%-10s | %-15s | %-20s | %s", 
                        order.getId(), order.getNomorMeja(), 
                        order.getWaktuPesan().format(formatter), order.getStatus()));
            }
        }
        
        if (!adaPesanan) {
            System.out.println("Anda belum memiliki pesanan.");
            return;
        }
        
        System.out.print("\nLihat detail pesanan (masukkan ID) atau kembali (tekan Enter): ");
        String id = scanner.nextLine();
        
        if (!id.isEmpty()) {
            for (Order order : orders) {
                if (order.getId().equals(id) && order.getNamaPelanggan().equals(currentUser.getNama())) {
                    System.out.println(order);
                    return;
                }
            }
            System.out.println("Pesanan dengan ID tersebut tidak ditemukan!");
        }
    }

    private static void lihatSemuaPesanan() {
        System.out.println("\n===== DAFTAR PESANAN =====");
        
        if (orders.isEmpty()) {
            System.out.println("Tidak ada pesanan.");
            return;
        }
        
        System.out.println(String.format("%-10s | %-15s | %-15s | %-20s | %s", 
                "ID", "Meja", "Pelanggan", "Waktu", "Status"));
        System.out.println("-------------------------------------------------------------------------");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (Order order : orders) {
            System.out.println(String.format("%-10s | %-15s | %-15s | %-20s | %s", 
                    order.getId(), order.getNomorMeja(), order.getNamaPelanggan(), 
                    order.getWaktuPesan().format(formatter), order.getStatus()));
        }
        
        System.out.print("\nLihat detail pesanan (masukkan ID) atau kembali (tekan Enter): ");
        String id = scanner.nextLine();
        
        if (!id.isEmpty()) {
            for (Order order : orders) {
                if (order.getId().equals(id)) {
                    System.out.println(order);
                    return;
                }
            }
            System.out.println("Pesanan dengan ID tersebut tidak ditemukan!");
        }
    }

    private static void buatPesananBaru() {
        System.out.println("\n===== BUAT PESANAN BARU =====");
        
        System.out.print("Nomor Meja: ");
        String nomorMeja = scanner.nextLine();
        
        Order newOrder = new Order(nomorMeja, currentUser.getNama());
        orders.add(newOrder);
        
        System.out.println("Pesanan baru berhasil dibuat dengan ID: " + newOrder.getId());
        
        System.out.print("Tambahkan item ke pesanan ini? (y/n): ");
        String tambahItem = scanner.nextLine();
        
        if (tambahItem.equalsIgnoreCase("y")) {
            tambahItemKePesananById(newOrder.getId());
        }
    }

    private static void tambahItemKePesanan() {
        System.out.println("\n===== TAMBAH ITEM KE PESANAN =====");
        
        if (orders.isEmpty()) {
            System.out.println("Anda belum memiliki pesanan aktif.");
            return;
        }
        
        System.out.println("Daftar Pesanan Aktif:");
        System.out.println(String.format("%-10s | %-15s | %s", 
                "ID", "Meja", "Status"));
        System.out.println("------------------------------------------");
        
        boolean adaPesanan = false;
        for (Order order : orders) {
            if (order.getNamaPelanggan().equals(currentUser.getNama()) && 
                !order.getStatus().equals("Selesai") && 
                !order.getStatus().equals("Dibatalkan")) {
                adaPesanan = true;
                System.out.println(String.format("%-10s | %-15s | %s", 
                        order.getId(), order.getNomorMeja(), order.getStatus()));
            }
        }
        
        if (!adaPesanan) {
            System.out.println("Anda tidak memiliki pesanan aktif.");
            return;
        }
        
        System.out.print("\nMasukkan ID pesanan: ");
        String id = scanner.nextLine();
        
        tambahItemKePesananById(id);
    }
    
    private static void tambahItemKePesananById(String orderId) {
        Order selectedOrder = null;
        for (Order order : orders) {
            if (order.getId().equals(orderId) && 
                (currentUser.getRole().equals("admin") || order.getNamaPelanggan().equals(currentUser.getNama()))) {
                selectedOrder = order;
                break;
            }
        }
        
        if (selectedOrder == null) {
            System.out.println("Pesanan dengan ID tersebut tidak ditemukan!");
            return;
        }
        
        if (selectedOrder.getStatus().equals("Selesai") || selectedOrder.getStatus().equals("Dibatalkan")) {
            System.out.println("Tidak dapat menambah item ke pesanan yang sudah selesai atau dibatalkan!");
            return;
        }
        
        boolean tambahLagi = true;
        while (tambahLagi) {
            lihatSemuaMenu();
            
            if (menuItems.isEmpty()) {
                return;
            }
            
            System.out.print("Masukkan ID menu yang ingin ditambahkan: ");
            String menuId = scanner.nextLine();
            
            MenuItem selectedItem = null;
            for (MenuItem item : menuItems) {
                if (item.getId().equals(menuId)) {
                    selectedItem = item;
                    break;
                }
            }
            
            if (selectedItem == null) {
                System.out.println("Menu dengan ID tersebut tidak ditemukan!");
                continue;
            }
            
            if (!selectedItem.isTersedia()) {
                System.out.println("Menu ini sedang tidak tersedia!");
                continue;
            }
            
            int jumlah = getIntInput("Jumlah: ");
            
            if (jumlah <= 0) {
                System.out.println("Jumlah harus lebih dari 0!");
                continue;
            }
            
            selectedOrder.tambahItem(selectedItem, jumlah);
            System.out.println("Item berhasil ditambahkan ke pesanan!");
            
            System.out.print("Tambah item lain? (y/n): ");
            String lanjut = scanner.nextLine();
            tambahLagi = lanjut.equalsIgnoreCase("y");
        }
    }

    private static void hapusItemDariPesanan() {
        System.out.println("\n===== HAPUS ITEM DARI PESANAN =====");
        
        if (orders.isEmpty()) {
            System.out.println("Anda belum memiliki pesanan aktif.");
            return;
        }
        
        System.out.println("Daftar Pesanan Aktif:");
        System.out.println(String.format("%-10s | %-15s | %-15s | %s", 
                "ID", "Meja", "Pelanggan", "Status"));
        System.out.println("-------------------------------------------------------");
        
        for (Order order : orders) {
            if (!order.getStatus().equals("Selesai") && !order.getStatus().equals("Dibatalkan")) {
                System.out.println(String.format("%-10s | %-15s | %-15s | %s", 
                        order.getId(), order.getNomorMeja(), order.getNamaPelanggan(), order.getStatus()));
            }
        }
        
        System.out.print("\nMasukkan ID pesanan: ");
        String orderId = scanner.nextLine();
        
        Order selectedOrder = null;
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                selectedOrder = order;
                break;
            }
        }
        
        if (selectedOrder == null) {
            System.out.println("Pesanan dengan ID tersebut tidak ditemukan!");
            return;
        }
        
        if (selectedOrder.getStatus().equals("Selesai") || selectedOrder.getStatus().equals("Dibatalkan")) {
            System.out.println("Tidak dapat menghapus item dari pesanan yang sudah selesai atau dibatalkan!");
            return;
        }
        
        if (selectedOrder.getItems().isEmpty()) {
            System.out.println("Pesanan ini tidak memiliki item!");
            return;
        }
        
        System.out.println("\nItem dalam pesanan:");
        System.out.println(String.format("%-10s | %-20s | %s | %s", 
                "ID", "Nama", "Jumlah", "Subtotal"));
        System.out.println("-------------------------------------------------------");
        
        for (OrderItem item : selectedOrder.getItems()) {
            System.out.println(String.format("%-10s | %-20s | %d | Rp %.2f", 
                    item.getMenuItem().getId(), item.getMenuItem().getNama(),
                    item.getJumlah(), item.getSubtotal()));
        }
        
        System.out.print("\nMasukkan ID menu yang ingin dihapus: ");
        String menuId = scanner.nextLine();
        
        if (selectedOrder.hapusItem(menuId)) {
            System.out.println("Item berhasil dihapus dari pesanan!");
        } else {
            System.out.println("Item dengan ID tersebut tidak ditemukan dalam pesanan!");
        }
    }

    private static void ubahStatusPesanan() {
        System.out.println("\n===== UBAH STATUS PESANAN =====");
        
        if (orders.isEmpty()) {
            System.out.println("Tidak ada pesanan.");
            return;
        }
        
        System.out.println("Daftar Pesanan:");
        System.out.println(String.format("%-10s | %-15s | %-15s | %-20s | %s", 
                "ID", "Meja", "Pelanggan", "Waktu", "Status"));
        System.out.println("-------------------------------------------------------------------------");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (Order order : orders) {
            System.out.println(String.format("%-10s | %-15s | %-15s | %-20s | %s", 
                    order.getId(), order.getNomorMeja(), order.getNamaPelanggan(), 
                    order.getWaktuPesan().format(formatter), order.getStatus()));
        }
        
        System.out.print("\nMasukkan ID pesanan: ");
        String orderId = scanner.nextLine();
        
        Order selectedOrder = null;
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                selectedOrder = order;
                break;
            }
        }
        
        if (selectedOrder == null) {
            System.out.println("Pesanan dengan ID tersebut tidak ditemukan!");
            return;
        }
        
        System.out.println("Status saat ini: " + selectedOrder.getStatus());
        System.out.println("Pilih status baru:");
        System.out.println("1. Baru");
        System.out.println("2. Diproses");
        System.out.println("3. Selesai");
        System.out.println("4. Dibatalkan");
        
        int pilihan = getIntInput("Pilih status: ");
        String newStatus;
        
        switch (pilihan) {
            case 1:
                newStatus = "Baru";
                break;
            case 2:
                newStatus = "Diproses";
                break;
            case 3:
                newStatus = "Selesai";
                break;
            case 4:
                newStatus = "Dibatalkan";
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }
        
        selectedOrder.setStatus(newStatus);
        System.out.println("Status pesanan berhasil diubah menjadi: " + newStatus);
    }
}
