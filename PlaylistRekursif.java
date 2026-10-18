/*
 * ============================================================
 *  TUGAS KELOMPOK 3 - Week 8
 *  Algorithmic Analysis, Mathematical Induction, and Recursive Functions
 *  Mata kuliah : COSC6025 - Data Structures and Algorithm Analysis
 * ------------------------------------------------------------
 *  Anggota Kelompok:
 *  1. 2902830385 - Muhamad Dzikri Fauzan
 *  Kelas         : COSC6025036
 *  Program       : PlaylistRekursif.java
 * ============================================================
 */

// CLASS LAGU

class Lagu {

    private String judul;
    private String artis;
    private double durasi;

    public Lagu(String judul, String artis, double durasi) {
        this.judul = judul;
        this.artis = artis;
        this.durasi = durasi;
    }

    public String getJudul() {
        return judul;
    }

    public String getArtis() {
        return artis;
    }

    public double getDurasi() {
        return durasi;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setArtis(String artis) {
        this.artis = artis;
    }

    public void setDurasi(double durasi) {
        if (durasi > 0) {
            this.durasi = durasi;
        } else {
            System.out.println("Durasi tidak valid!");
        }
    }

    public void tampilkanInfo() {
        System.out.println("Judul  : " + judul);
        System.out.println("Artis  : " + artis);
        System.out.println("Durasi : " + durasi + " menit");
    }
}

// CLASS PLAYLISTREKURSIF

public class PlaylistRekursif {

    static Lagu[] playlist;

   
    static double totalDurasi(Lagu[] list, int n) {
        if (n == 0) {
            return 0;
        }
        return list[n - 1].getDurasi() + totalDurasi(list, n - 1);
    }

   
    static void tampilkanMundur(Lagu[] list, int index) {
        tampilkanMundur(list, index, true);
    }

    static void tampilkanMundur(Lagu[] list, int index, boolean cetak) {
        if (index < 0) {
            return;
        }
        if (cetak) {
            System.out.println((index + 1) + ". " + list[index].getJudul()
                    + " - " + list[index].getArtis()
                    + " (" + String.format("%.2f", list[index].getDurasi()) + ")");
        }
        tampilkanMundur(list, index - 1, cetak);
    }

    
    static double cariDurasiTerpanjang(Lagu[] list, int index) {
        if (index == list.length - 1) {
            return list[index].getDurasi();
        }
        return Math.max(list[index].getDurasi(),
                cariDurasiTerpanjang(list, index + 1));
    }

    static Lagu cariLagu(double durasi) {
        for (Lagu l : playlist) {
            if (l.getDurasi() == durasi) {
                return l;
            }
        }
        return null;
    }

    static String ms(long nano) {
        return String.format("%.5f ms", nano / 1_000_000.0);
    }

    static Lagu[] buatPlaylist(int n) {
        Lagu[] dasar = {
            new Lagu("Perfect", "Ed Sheeran", 4.23),
            new Lagu("Shivers", "Ed Sheeran", 3.50),
            new Lagu("Fix You", "Coldplay", 4.52),
            new Lagu("Yellow", "Coldplay", 4.29),
            new Lagu("Viva La Vida", "Coldplay", 4.02)
        };
        Lagu[] hasil = new Lagu[n];
        for (int i = 0; i < n; i++) {
            Lagu b = dasar[i % dasar.length];
            hasil[i] = new Lagu(b.getJudul(), b.getArtis(), b.getDurasi());
        }
        return hasil;
    }

   
    static long[] benchmark(int n, int ulang) {
        Lagu[] data = buatPlaylist(n);
        for (int i = 0; i < 2000; i++) {
            totalDurasi(data, n);
            tampilkanMundur(data, n - 1, false);
            cariDurasiTerpanjang(data, 0);
        }
        long t1 = 0, t2 = 0, t3 = 0, s;
        for (int i = 0; i < ulang; i++) {
            s = System.nanoTime();
            totalDurasi(data, n);
            t1 += System.nanoTime() - s;

            s = System.nanoTime();
            tampilkanMundur(data, n - 1, false);
            t2 += System.nanoTime() - s;

            s = System.nanoTime();
            cariDurasiTerpanjang(data, 0);
            t3 += System.nanoTime() - s;
        }
        return new long[] { t1 / ulang, t2 / ulang, t3 / ulang };
    }

    public static void main(String[] args) {
        playlist = buatPlaylist(5);
        int n = playlist.length;

        long mulai = System.nanoTime();
        double total = totalDurasi(playlist, n);
        long wTotal = System.nanoTime() - mulai;

        mulai = System.nanoTime();
        double terpanjang = cariDurasiTerpanjang(playlist, 0);
        long wCari = System.nanoTime() - mulai;

        Lagu lt = cariLagu(terpanjang);

        System.out.println("=== ANALISIS REKURSIF PLAYLIST ===");
        System.out.println("Jumlah lagu : " + n);
        System.out.printf("Total durasi : %.2f menit%n", total);
        System.out.printf("Lagu terpanjang : \"%s\" - %s (%.2f menit)%n",
                lt.getJudul(), lt.getArtis(), terpanjang);

        System.out.println("\nDaftar lagu (ditampilkan terbalik):");
        mulai = System.nanoTime();
        tampilkanMundur(playlist, n - 1);
        long wMundur = System.nanoTime() - mulai;

        System.out.println();
        System.out.println("Execution Time (totalDurasi): " + ms(wTotal));
        System.out.println("Execution Time (tampilkanMundur): " + ms(wMundur));
        System.out.println("Execution Time (cariDurasiTerpanjang): " + ms(wCari));

        System.out.println("\n=== EKSPERIMEN WAKTU EKSEKUSI (rata-rata 10.000 kali) ===");
        System.out.printf("%-8s %-18s %-22s %-24s%n", "n",
                "totalDurasi()", "tampilkanMundur()", "cariDurasiTerpanjang()");
        int[] ukuran = { 3, 5, 10, 100, 1000, 5000 };
        for (int u : ukuran) {
            long[] w = benchmark(u, 10000);
            System.out.printf("%-8d %-18s %-22s %-24s%n", u,
                    ms(w[0]), ms(w[1]), ms(w[2]));
        }
        System.out.println("\n(Catatan: tampilkanMundur diukur tanpa cetak ke layar"
                + " pada eksperimen agar yang diukur hanya rekursinya.)");
    }
}
