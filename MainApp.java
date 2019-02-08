import sun.security.util.Length;

public class MainApp {

    static final int size = 100000;
    static final int h = size / 2;

    private static class halfArrayCalc implements Runnable {
        float[] array;
        int delta;          //заполнение ополовиненного массива

        halfArrayCalc(float[] arr, int delta){
            this.array = arr;       //перелаем в конструктор ссылку на сам массив и на
            this.delta = delta;     //сколько индекс отличается от индеска в оригинальном массиве
        }

        @Override
        public void run() {
            for (int i = delta; i < array.length + delta; i++) {
                array[i - delta] = (float) (array[i - delta] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        }
    }
    public static void main(String[] args) {
        float[] arr = new float[size];


        for (int i = 0; i < size; i++){     //заполняем единицами
            arr[i] = 1;
        }
        System.out.printf("Один поток %d%n", singleThreadCalc(arr));
        System.out.println(arr[h]);
        for (int i = 0; i < size; i++){
            arr[i] = 1;
        }

        System.out.printf("Два потока %d%n", doubleThreadCalc(arr));
        System.out.println(arr[h]);

    }

    private static long singleThreadCalc(float[] arr){
        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++){
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        return System.currentTimeMillis() - a;
    }

    private static long doubleThreadCalc(float[] arr){
        long a = System.currentTimeMillis();
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);

        Thread T1 = new Thread(new halfArrayCalc(a1, 0));
        T1.start();
        Thread T2 = new Thread(new halfArrayCalc(a2, h));
        T2.start();

        try {
            T1.join();
            T2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);
        return System.currentTimeMillis() - a;
    }

}
