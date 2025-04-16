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
    
    public String getDeskripsi() {
        return "Menu Regular";
    }
}

class KopiItem extends MenuItem {
    private String jenisKopi;
    private String tingkatRoasting;
    
    public KopiItem(String nama, double harga, String jenisKopi, String tingkatRoasting) {
        super(nama, "Kopi", harga);
        this.jenisKopi = jenisKopi;
        this.tingkatRoasting = tingkatRoasting;
    }
    
    public String getJenisKopi() { return jenisKopi; }
    
    public void setJenisKopi(String jenisKopi) {
        if (jenisKopi == null || jenisKopi.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis kopi tidak boleh kosong!");
        }
        this.jenisKopi = jenisKopi;
    }
    
    public String getTingkatRoasting() { return tingkatRoasting; }
    
    public void setTingkatRoasting(String tingkatRoasting) {
        if (tingkatRoasting == null || tingkatRoasting.trim().isEmpty()) {
            throw new IllegalArgumentException("Tingkat roasting tidak boleh kosong!");
        }
        this.tingkatRoasting = tingkatRoasting;
    }
    
    @Override
    public String getDeskripsi() {
        return String.format("%s (Jenis: %s, Roasting: %s)", getNama(), jenisKopi, tingkatRoasting);
    }
    
    @Override
    public String toString() {
        return String.format("%s | Jenis: %s | Roasting: %s", 
                super.toString(), jenisKopi, tingkatRoasting);
    }
}

class MakananItem extends MenuItem {
    private boolean isVegetarian;
    private String topping; 
    
    private final String[] TOPPING_VALID = {"Coklat", "Keju", "Kacang", "Buah", "Krim", "Tanpa Topping"};
    
    public MakananItem(String nama, double harga, boolean isVegetarian, String topping) {
        super(nama, "Makanan", harga);
        this.isVegetarian = isVegetarian;
        setTopping(topping); 
    }
    
    public boolean isVegetarian() { return isVegetarian; }
    
    public void setVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }
    
    public String getTopping() { return topping; }
    
    public void setTopping(String topping) {
        for (String t : TOPPING_VALID) {
            if (t.equalsIgnoreCase(topping)) {
                this.topping = t;
                return;
            }
        }
        throw new IllegalArgumentException("Topping tidak valid! Pilihan: " + String.join(", ", TOPPING_VALID));
    }
    
    @Override
    public String getDeskripsi() {
        String vegetarianStatus = isVegetarian ? "Vegetarian" : "Non-Vegetarian";
        return String.format("%s (%s, Topping: %s)", getNama(), vegetarianStatus, topping);
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | Topping: %s", 
                super.toString(), isVegetarian ? "Vegetarian" : "Non-Vegetarian", topping);
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

    public void tambahItem(MenuItem menuItem) {
        tambahItem(menuItem, 1);
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
    
    public String getInfo() {
        return String.format("Username: %s | Nama: %s | Role: %s", username, nama, role);
    }
    
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}

class Admin extends User {
    private String departemen;
    private int levelAkses;
    
    public Admin(String username, String password, String nama, String departemen, int levelAkses) {
        super(username, password, "admin", nama);
        this.departemen = departemen;
        setLevelAkses(levelAkses);
    }
    
    public String getDepartemen() { return departemen; }
    
    public void setDepartemen(String departemen) {
        if (departemen == null || departemen.trim().isEmpty()) {
            throw new IllegalArgumentException("Departemen tidak boleh kosong!");
        }
        this.departemen = departemen;
    }
    
    public int getLevelAkses() { return levelAkses; }
    
    public void setLevelAkses(int levelAkses) {
        if (levelAkses < 1 || levelAkses > 3) {
            throw new IllegalArgumentException("Level akses harus antara 1-3!");
        }
        this.levelAkses = levelAkses;
    }
    
    @Override
    public String getInfo() {
        return String.format("%s | Departemen: %s | Level Akses: %d", 
                super.getInfo(), departemen, levelAkses);
    }
    
    public boolean bisaAksesLaporan() {
        return levelAkses >= 2;
    }
    
    public boolean bisaManagePengguna() {
        return levelAkses == 3;
    }
}

class Pelanggan extends User {
    private String nomorTelepon;
    private int poin;
    private String level;
    
    public Pelanggan(String username, String password, String nama, String nomorTelepon) {
        super(username, password, "pelanggan", nama);
        this.nomorTelepon = nomorTelepon;
        this.poin = 0;
        updateLevel();
    }
    
    public String getNomorTelepon() { return nomorTelepon; }
    
    public void setNomorTelepon(String nomorTelepon) {
        if (nomorTelepon == null || nomorTelepon.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor telepon tidak boleh kosong!");
        }
        this.nomorTelepon = nomorTelepon;
    }
    
    public int getPoin() { return poin; }
    
    public void tambahPoin(int poin) {
        if (poin < 0) {
            throw new IllegalArgumentException("Poin tidak boleh negatif!");
        }
        this.poin += poin;
        updateLevel();
    }
    
    public String getLevel() { return level; }
    
    private void updateLevel() {
        if (poin < 100) {
            level = "Bronze";
        } else if (poin < 300) {
            level = "Silver";
        } else if (poin < 500) {
            level = "Gold";
        } else {
            level = "Platinum";
        }
    }
    
    @Override
    public String getInfo() {
        return String.format("%s | Telepon: %s | Poin: %d | Level: %s", 
                super.getInfo(), nomorTelepon, poin, level);
    }
    
    public double hitungDiskon() {
        switch (level) {
            case "Bronze": return 0.0;
            case "Silver": return 0.05;
            case "Gold": return 0.1;
            case "Platinum": return 0.15;
            default: return 0.0;
        }
    }
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
                        kelolaUser();
                        break;
                    case 4:
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
                        lihatProfilPelanggan();
                        break;
                    case 4:
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
        menuItems.add(new KopiItem("Espresso", 18000, "Arabica", "Medium"));
        menuItems.add(new KopiItem("Cappuccino", 25000, "Blend", "Medium-Dark"));
        menuItems.add(new KopiItem("Latte", 23000, "Arabica", "Light"));
        menuItems.add(new KopiItem("Americano", 20000, "Robusta", "Dark"));
        menuItems.add(new MakananItem("Croissant", 15000, false, "Tanpa Topping"));
        menuItems.add(new MakananItem("Cheesecake", 28000, true, "Buah"));
        menuItems.add(new MenuItem("Air Mineral", "Minuman", 8000));
        menuItems.add(new MinumanItem("Kopi", 10000, true, 3));
        menuItems.add(new MinumanItem("Kopi Susu", 15000, false, 2));
    
        users.put("admin", new Admin("admin", "admin123", "Administrator", "Management", 3));
        users.put("pelanggan", new Pelanggan("pelanggan", "pelanggan123", "Pelanggan Umum", "081234567890"));
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
        if (user != null && user.authenticate(password)) {
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
        System.out.print("Nomor Telepon: ");
        String nomorTelepon = scanner.nextLine();
        
        User newUser = new Pelanggan(username, password, nama, nomorTelepon);
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
        System.out.println("3. Kelola User");
        System.out.println("4. Logout");
    }
    
    private static void tampilkanMenuPelanggan() {
        System.out.println("\n===== MENU PELANGGAN =====");
        System.out.println("1. Lihat Menu");
        System.out.println("2. Pesanan");
        System.out.println("3. Lihat Profil");
        System.out.println("4. Logout");
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
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        
        for (MenuItem item : menuItems) {
            System.out.println(item);
            System.out.println("Deskripsi: " + item.getDeskripsi());
            System.out.println("--------------------------------------------------------------------------------------------------------------");
        }
        
        if (currentUser != null && currentUser.getRole().equals("pelanggan")) {
            System.out.print("\nTekan Enter untuk kembali...");
            scanner.nextLine();
        }
    }

    private static void tambahMenu() {
        System.out.println("\n===== TAMBAH MENU BARU =====");
        System.out.println("Pilih jenis menu:");
        System.out.println("1. Menu Kopi");
        System.out.println("2. Menu Makanan");
        System.out.println("3. Menu Minuman Reguler");
        System.out.println("4. Menu Minuman Spesial");
        
        int jenisMenu = getIntInput("Pilih jenis menu: ");
        
        switch (jenisMenu) {
            case 1:
                tambahMenuKopi();
                break;
            case 2:
                tambahMenuMakanan();
                break;
            case 3:
                tambahMenuRegular();
                break;
                case 4: 
                    tambahMenuMinumanSpesial();
                    break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }
    
    private static void tambahMenuKopi() {
        System.out.println("\n===== TAMBAH MENU KOPI =====");
        
        System.out.print("Nama kopi: ");
        String nama = scanner.nextLine();
        
        double harga = getDoubleInput("Harga (Rp): ");
        
        System.out.print("Jenis kopi (Arabica/Robusta/Blend): ");
        String jenisKopi = scanner.nextLine();
        
        System.out.print("Tingkat roasting (Light/Medium/Dark): ");
        String tingkatRoasting = scanner.nextLine();
        
        MenuItem newItem = new KopiItem(nama, harga, jenisKopi, tingkatRoasting);
        menuItems.add(newItem);
        
        System.out.println("Menu kopi berhasil ditambahkan!");
    }
    
    private static void tambahMenuMakanan() {
        System.out.println("\n===== TAMBAH MENU MAKANAN =====");
        
        System.out.print("Nama makanan: ");
        String nama = scanner.nextLine();
        
        double harga = getDoubleInput("Harga (Rp): ");
        
        System.out.print("Vegetarian? (y/n): ");
        boolean isVegetarian = scanner.nextLine().equalsIgnoreCase("y");
        
        System.out.println("Pilihan topping: Coklat, Keju, Kacang, Buah, Krim, Tanpa Topping");
        System.out.print("Topping: ");
        String topping = scanner.nextLine();
        
        MenuItem newItem = new MakananItem(nama, harga, isVegetarian, topping);
        menuItems.add(newItem);
        
        System.out.println("Menu makanan berhasil ditambahkan!");
    }
    
    private static void tambahMenuRegular() {
        System.out.println("\n===== TAMBAH MENU REGULAR =====");
        
        System.out.print("Nama menu: ");
        String nama = scanner.nextLine();
        
        System.out.print("Kategori (Kopi/Makanan/Minuman): ");
        String kategori = scanner.nextLine();
        
        double harga = getDoubleInput("Harga (Rp): ");
        
        MenuItem newItem = new MenuItem(nama, kategori, harga);
        menuItems.add(newItem);
        
        System.out.println("Menu regular berhasil ditambahkan!");
    }

    private static void tambahMenuMinumanSpesial() {
        System.out.println("\n===== TAMBAH MENU MINUMAN SPESIAL =====");
        
        System.out.print("Nama minuman: ");
        String nama = scanner.nextLine();
        
        double harga = getDoubleInput("Harga (Rp): ");
        
        System.out.print("Dingin? (y/n): ");
        boolean isDingin = scanner.nextLine().equalsIgnoreCase("y");
        
        int levelManis = getIntInput("Level kemanisan (0-5): ");
        
        MenuItem newItem = new MinumanItem(nama, harga, isDingin, levelManis);
        menuItems.add(newItem);
        
        System.out.println("Menu minuman spesial berhasil ditambahkan!");
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
        
        if (itemToEdit instanceof KopiItem) {
            editMenuKopi((KopiItem) itemToEdit);
        } else if (itemToEdit instanceof MakananItem) {
            editMenuMakanan((MakananItem) itemToEdit);
        } else if (itemToEdit instanceof MinumanItem) {
            editMenuMinuman((MinumanItem) itemToEdit);
        } else {
            editMenuRegular(itemToEdit);
        }
    }

    private static void editMenuMinuman(MinumanItem item) {
        System.out.print("Nama baru (kosongkan jika tidak ingin mengubah): ");
        String newNama = scanner.nextLine();
        if (!newNama.isEmpty()) {
            item.setNama(newNama);
        }
        
        System.out.print("Ubah harga? (y/n): ");
        String ubahHarga = scanner.nextLine();
        if (ubahHarga.equalsIgnoreCase("y")) {
            double newHarga = getDoubleInput("Harga baru (Rp): ");
            item.setHarga(newHarga);
        }
        
        System.out.print("Ubah status dingin? (y/n): ");
        String ubahDingin = scanner.nextLine();
        if (ubahDingin.equalsIgnoreCase("y")) {
            System.out.print("Dingin? (y/n): ");
            boolean newDingin = scanner.nextLine().equalsIgnoreCase("y");
            item.setDingin(newDingin);
        }
        
        System.out.print("Ubah level kemanisan? (y/n): ");
        String ubahManis = scanner.nextLine();
        if (ubahManis.equalsIgnoreCase("y")) {
            int newLevel = getIntInput("Level kemanisan baru (0-5): ");
            item.setLevelKemanisan(newLevel);
        }
        
        System.out.print("Ubah ketersediaan? (y/n): ");
        String ubahStatus = scanner.nextLine();
        if (ubahStatus.equalsIgnoreCase("y")) {
            System.out.print("Status baru (tersedia/tidak tersedia): ");
            boolean newStatus = scanner.nextLine().equalsIgnoreCase("tersedia");
            item.setTersedia(newStatus); // Akan memanggil override setTersedia
        }
        
        System.out.println("Menu minuman berhasil diperbarui!");
    }
    
    private static void editMenuKopi(KopiItem item) {
        System.out.print("Nama baru (kosongkan jika tidak ingin mengubah): ");
        String newNama = scanner.nextLine();
        if (!newNama.isEmpty()) {
            item.setNama(newNama);
        }
        
        System.out.print("Ubah harga? (y/n): ");
        String ubahHarga = scanner.nextLine();
        if (ubahHarga.equalsIgnoreCase("y")) {
            double newHarga = getDoubleInput("Harga baru (Rp): ");
            item.setHarga(newHarga);
        }
        
        System.out.print("Jenis kopi baru (kosongkan jika tidak ingin mengubah): ");
        String newJenisKopi = scanner.nextLine();
        if (!newJenisKopi.isEmpty()) {
            item.setJenisKopi(newJenisKopi);
        }
        
        System.out.print("Tingkat roasting baru (kosongkan jika tidak ingin mengubah): ");
        String newRoasting = scanner.nextLine();
        if (!newRoasting.isEmpty()) {
            item.setTingkatRoasting(newRoasting);
        }
        
        System.out.print("Ubah ketersediaan? (y/n): ");
        String ubahStatus = scanner.nextLine();
        if (ubahStatus.equalsIgnoreCase("y")) {
            System.out.print("Status baru (tersedia/tidak tersedia): ");
            boolean newStatus = scanner.nextLine().equalsIgnoreCase("tersedia");
            item.setTersedia(newStatus);
        }
        
        System.out.println("Menu kopi berhasil diperbarui!");
    }
    
    private static void editMenuMakanan(MakananItem item) {
        System.out.print("Nama baru (kosongkan jika tidak ingin mengubah): ");
        String newNama = scanner.nextLine();
        if (!newNama.isEmpty()) {
            item.setNama(newNama);
        }
        
        System.out.print("Ubah harga? (y/n): ");
        String ubahHarga = scanner.nextLine();
        if (ubahHarga.equalsIgnoreCase("y")) {
            double newHarga = getDoubleInput("Harga baru (Rp): ");
            item.setHarga(newHarga);
        }
        
        System.out.print("Ubah status vegetarian? (y/n): ");
        String ubahVegetarian = scanner.nextLine();
        if (ubahVegetarian.equalsIgnoreCase("y")) {
            System.out.print("Vegetarian? (y/n): ");
            boolean newVegetarian = scanner.nextLine().equalsIgnoreCase("y");
            item.setVegetarian(newVegetarian);
        }
        
        System.out.print("Ubah topping? (y/n): ");
        String ubahTopping = scanner.nextLine();
        if (ubahTopping.equalsIgnoreCase("y")) {
            System.out.println("Pilihan topping: Coklat, Keju, Kacang, Buah, Krim, Tanpa Topping");
            System.out.print("Topping baru: ");
            String newTopping = scanner.nextLine();
            item.setTopping(newTopping);
        }
        
        System.out.print("Ubah ketersediaan? (y/n): ");
        String ubahStatus = scanner.nextLine();
        if (ubahStatus.equalsIgnoreCase("y")) {
            System.out.print("Status baru (tersedia/tidak tersedia): ");
            boolean newStatus = scanner.nextLine().equalsIgnoreCase("tersedia");
            item.setTersedia(newStatus);
        }
        
        System.out.println("Menu makanan berhasil diperbarui!");
    }
    
    private static void editMenuRegular(MenuItem item) {
        System.out.print("Nama baru (kosongkan jika tidak ingin mengubah): ");
        String newNama = scanner.nextLine();
        if (!newNama.isEmpty()) {
            item.setNama(newNama);
        }
        
        System.out.print("Ubah kategori? (y/n): ");
        String ubahKategori = scanner.nextLine();
        if (ubahKategori.equalsIgnoreCase("y")) {
            System.out.print("Kategori baru (Kopi/Makanan/Minuman): ");
            String newKategori = scanner.nextLine();
            item.setKategori(newKategori);
        }
        
        System.out.print("Ubah harga? (y/n): ");
        String ubahHarga = scanner.nextLine();
        if (ubahHarga.equalsIgnoreCase("y")) {
            double newHarga = getDoubleInput("Harga baru (Rp): ");
            item.setHarga(newHarga);
        }
        
        System.out.print("Ubah ketersediaan? (y/n): ");
        String ubahStatus = scanner.nextLine();
        if (ubahStatus.equalsIgnoreCase("y")) {
            System.out.print("Status baru (tersedia/tidak tersedia): ");
            boolean newStatus = scanner.nextLine().equalsIgnoreCase("tersedia");
            item.setTersedia(newStatus);
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
        System.out.print("Apakah Anda yakin ingin menghapus menu ini? (y/n): ");
        String konfirmasi = scanner.nextLine();
        
        if (konfirmasi.equalsIgnoreCase("y")) {
            menuItems.remove(itemToRemove);
            System.out.println("Menu berhasil dihapus!");
        } else {
            System.out.println("Penghapusan menu dibatalkan.");
        }
    }

    private static void kelolaUser() {
        Admin adminUser = (Admin) currentUser;
        if (!adminUser.bisaManagePengguna()) {
            System.out.println("Anda tidak memiliki akses untuk mengelola pengguna!");
            return;
        }
        
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n===== KELOLA USER =====");
            System.out.println("1. Lihat Semua User");
            System.out.println("2. Tambah Admin Baru");
            System.out.println("3. Hapus User");
            System.out.println("4. Kembali ke Menu Admin");
            
            int pilihan = getIntInput("Pilih menu: ");
            
            switch (pilihan) {
                case 1:
                    lihatSemuaUser();
                    break;
                case 2:
                    tambahAdmin();
                    break;
                case 3:
                    hapusUser();
                    break;
                case 4:
                    kembali = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private static void lihatSemuaUser() {
        System.out.println("\n===== DAFTAR USER =====");
        
        if (users.isEmpty()) {
            System.out.println("Tidak ada user terdaftar.");
            return;
        }
        
        for (User user : users.values()) {
            System.out.println(user.getInfo());
            System.out.println("---------------------------------------------------");
        }
    }
    
    private static void tambahAdmin() {
        System.out.println("\n===== TAMBAH ADMIN BARU =====");
        
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
        System.out.print("Departemen: ");
        String departemen = scanner.nextLine();
        
        int levelAkses = getIntInput("Level Akses (1-3): ");
        
        User newAdmin = new Admin(username, password, nama, departemen, levelAkses);
        users.put(username, newAdmin);
        
        System.out.println("Admin baru berhasil ditambahkan!");
    }
    
    private static void hapusUser() {
        System.out.println("\n===== HAPUS USER =====");
        lihatSemuaUser();
        
        System.out.print("Masukkan username yang akan dihapus: ");
        String username = scanner.nextLine();
        
        if (username.equals(currentUser.getUsername())) {
            System.out.println("Anda tidak dapat menghapus akun Anda sendiri!");
            return;
        }
        
        if (!users.containsKey(username)) {
            System.out.println("User dengan username tersebut tidak ditemukan!");
            return;
        }
        
        System.out.println("User yang akan dihapus: " + users.get(username).getInfo());
        System.out.print("Apakah Anda yakin ingin menghapus user ini? (y/n): ");
        String konfirmasi = scanner.nextLine();
        
        if (konfirmasi.equalsIgnoreCase("y")) {
            users.remove(username);
            System.out.println("User berhasil dihapus!");
        } else {
            System.out.println("Penghapusan user dibatalkan.");
        }
    }

    private static void kelolaPesanan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n===== KELOLA PESANAN =====");
            System.out.println("1. Buat Pesanan Baru");
            System.out.println("2. Lihat Pesanan Saya");
            System.out.println("3. Kembali ke Menu Pelanggan");
            
            int pilihan = getIntInput("Pilih menu: ");
            
            switch (pilihan) {
                case 1:
                    buatPesananBaru();
                    break;
                case 2:
                    lihatPesananSaya();
                    break;
                case 3:
                    kembali = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private static void buatPesananBaru() {
        System.out.println("\n===== BUAT PESANAN BARU =====");
        
        System.out.print("Nomor Meja: ");
        String nomorMeja = scanner.nextLine();
        
        Order pesananBaru = new Order(nomorMeja, currentUser.getNama());
        
        boolean selesaiMemesan = false;
        while (!selesaiMemesan) {
            lihatSemuaMenu();
            
            System.out.println("\n1. Tambah Item ke Pesanan");
            System.out.println("2. Lihat Pesanan Saat Ini");
            System.out.println("3. Hapus Item dari Pesanan");
            System.out.println("4. Selesai dan Simpan Pesanan");
            System.out.println("5. Batal");
            
            int pilihan = getIntInput("Pilih menu: ");
            
            switch (pilihan) {
                case 1:
                System.out.println("\n=== TAMBAH ITEM ===");
                System.out.println("1. Tambah dengan jumlah spesifik");
                System.out.println("2. Tambah 1 item (default)");
                int pilihanTambah = getIntInput("Pilih cara menambahkan: ");
                
                if (pilihanTambah == 1) {
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
                        break;
                    }
                    
                    if (!selectedItem.isTersedia()) {
                        System.out.println("Maaf, menu ini sedang tidak tersedia!");
                        break;
                    }
                    
                    int jumlah = getIntInput("Jumlah: ");
                    
                    if (jumlah <= 0) {
                        System.out.println("Jumlah harus lebih dari 0!");
                        break;
                    }
                    
                    pesananBaru.tambahItem(selectedItem, jumlah);
                    System.out.println(jumlah + "x " + selectedItem.getNama() + " berhasil ditambahkan ke pesanan!");
                } else if (pilihanTambah == 2) {
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
                        break;
                    }
                    
                    if (!selectedItem.isTersedia()) {
                        System.out.println("Maaf, menu ini sedang tidak tersedia!");
                        break;
                    }
                    
                    pesananBaru.tambahItem(selectedItem); 
                    System.out.println("1x " + selectedItem.getNama() + " berhasil ditambahkan ke pesanan!");
                } else {
                    System.out.println("Pilihan tidak valid!");
                }
                    break;
                case 2:
                    System.out.println(pesananBaru);
                    break;
                case 3:
                    hapusItemDariPesanan(pesananBaru);
                    break;
                case 4:
                    if (pesananBaru.getItems().isEmpty()) {
                        System.out.println("Pesanan kosong! Tambahkan item terlebih dahulu.");
                    } else {
                        orders.add(pesananBaru);
                        System.out.println("Pesanan berhasil disimpan!");
                        
                        if (currentUser instanceof Pelanggan) {
                            int poinDiperoleh = (int) (pesananBaru.hitungTotal() / 10000);
                            ((Pelanggan) currentUser).tambahPoin(poinDiperoleh);
                            System.out.println("Anda mendapatkan " + poinDiperoleh + " poin!");
                        }
                        
                        selesaiMemesan = true;
                    }
                    break;
                case 5:
                    System.out.println("Pembuatan pesanan dibatalkan.");
                    selesaiMemesan = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
    
    private static void tambahItemKePesanan(Order pesanan) {
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
            return;
        }
        
        if (!selectedItem.isTersedia()) {
            System.out.println("Maaf, menu ini sedang tidak tersedia!");
            return;
        }
        
        int jumlah = getIntInput("Jumlah: ");
        
        if (jumlah <= 0) {
            System.out.println("Jumlah harus lebih dari 0!");
            return;
        }
        
        pesanan.tambahItem(selectedItem, jumlah);
        System.out.println(jumlah + "x " + selectedItem.getNama() + " berhasil ditambahkan ke pesanan!");
    }
    
    private static void hapusItemDariPesanan(Order pesanan) {
        if (pesanan.getItems().isEmpty()) {
            System.out.println("Pesanan kosong!");
            return;
        }
        
        System.out.println("Daftar item dalam pesanan:");
        int index = 1;
        for (OrderItem item : pesanan.getItems()) {
            System.out.println(index + ". " + item.getMenuItem().getNama() + " - " + item.getJumlah() + "x");
            index++;
        }
        
        System.out.print("Masukkan ID menu yang ingin dihapus: ");
        String menuId = scanner.nextLine();
        
        if (pesanan.hapusItem(menuId)) {
            System.out.println("Item berhasil dihapus dari pesanan!");
        } else {
            System.out.println("Item dengan ID tersebut tidak ditemukan dalam pesanan!");
        }
    }
    
    private static void lihatPesananSaya() {
        System.out.println("\n===== PESANAN SAYA =====");
        
        boolean adaPesanan = false;
        for (Order order : orders) {
            if (order.getNamaPelanggan().equals(currentUser.getNama())) {
                System.out.println(order);
                adaPesanan = true;
            }
        }
        
        if (!adaPesanan) {
            System.out.println("Anda belum memiliki pesanan.");
        }
        
        System.out.print("\nTekan Enter untuk kembali...");
        scanner.nextLine();
    }
    
    private static void lihatSemuaPesanan() {
        System.out.println("\n===== SEMUA PESANAN =====");
        
        if (orders.isEmpty()) {
            System.out.println("Tidak ada pesanan.");
            return;
        }
        
        for (Order order : orders) {
            System.out.println(order);
            System.out.println("-----------------------------------------------------------------------");
        }
    }
    
    private static void ubahStatusPesanan() {
        System.out.println("\n===== UBAH STATUS PESANAN =====");
        
        if (orders.isEmpty()) {
            System.out.println("Tidak ada pesanan.");
            return;
        }
        
        System.out.println(String.format("%-5s | %-20s | %-15s | %-10s", 
                "ID", "Pelanggan", "Meja", "Status"));
        System.out.println("-----------------------------------------------------");
        
        for (Order order : orders) {
            System.out.println(String.format("%-5s | %-20s | %-15s | %-10s", 
                    order.getId(), order.getNamaPelanggan(), order.getNomorMeja(), order.getStatus()));
        }
        
        System.out.print("\nMasukkan ID pesanan yang ingin diubah statusnya: ");
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
        
        System.out.println("Pesanan yang dipilih: " + selectedOrder.getId() + " - " + selectedOrder.getStatus());
        System.out.println("1. Diproses");
        System.out.println("2. Selesai");
        System.out.println("3. Dibatalkan");
        
        int pilihan = getIntInput("Pilih status baru: ");
        
        switch (pilihan) {
            case 1:
                selectedOrder.setStatus("Diproses");
                break;
            case 2:
                selectedOrder.setStatus("Selesai");
                break;
            case 3:
                selectedOrder.setStatus("Dibatalkan");
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }
        
        System.out.println("Status pesanan berhasil diubah!");
    }
    
    private static void lihatProfilPelanggan() {
        System.out.println("\n===== PROFIL PELANGGAN =====");
        Pelanggan pelanggan = (Pelanggan) currentUser;
        
        System.out.println("Nama: " + pelanggan.getNama());
        System.out.println("Username: " + pelanggan.getUsername());
        System.out.println("Nomor Telepon: " + pelanggan.getNomorTelepon());
        System.out.println("Poin: " + pelanggan.getPoin());
        System.out.println("Level: " + pelanggan.getLevel());
        System.out.println("Diskon: " + (pelanggan.hitungDiskon() * 100) + "%");
        
        System.out.print("\nTekan Enter untuk kembali...");
        scanner.nextLine();
    }

    static class MinumanItem extends MenuItem {
        private boolean isDingin;
        private int levelKemanisan;
        
        public MinumanItem(String nama, double harga, boolean isDingin, int levelKemanisan) {
            super(nama, "Minuman", harga);
            this.isDingin = isDingin;
            setLevelKemanisan(levelKemanisan);
        }
        
        public boolean isDingin() { return isDingin; }
        
        public void setDingin(boolean isDingin) {
            this.isDingin = isDingin;
        }
        
        public int getLevelKemanisan() { return levelKemanisan; }
        
        public void setLevelKemanisan(int levelKemanisan) {
            if (levelKemanisan < 0 || levelKemanisan > 5) {
                throw new IllegalArgumentException("Level kemanisan harus antara 0-5!");
            }
            this.levelKemanisan = levelKemanisan;
        }
        
        @Override
        public String getDeskripsi() {
            return String.format("%s (%s, Level Manis: %d/5)", 
                    getNama(), isDingin ? "Dingin" : "Panas", levelKemanisan);
        }
        
        @Override
        public String toString() {
            return String.format("%s | %s | Level Manis: %d/5", 
                    super.toString(), isDingin ? "Dingin" : "Panas", levelKemanisan);
        }

        @Override
        public void setTersedia(boolean tersedia) {
            super.setTersedia(tersedia);
            System.out.println("Status ketersediaan minuman " + getNama() + " diubah menjadi: " + (tersedia ? "Tersedia" : "Tidak Tersedia"));
        }
    }
}