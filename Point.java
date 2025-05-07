
class Point {
    String name;
    double x_c;
    double y_c;
    boolean[] thread_flag;

    Point(int threads) {
        thread_flag = new boolean[threads];
    }
}
