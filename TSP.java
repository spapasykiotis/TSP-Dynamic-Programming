
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TSP extends Thread {
    static int tn; // Total number of threads
    int pass_flag;

    static ArrayList<Point> p = new ArrayList<>();
    static Point sp;
    static int isp, n;

    static double[] min;
    static ArrayList<Double>[] answer;
    static ConcurrentHashMap<String, ArrayList<Double>> map = new ConcurrentHashMap<>();

    TSP(int x) {
        this.pass_flag = x;
    }

    @SuppressWarnings("unchecked")
    static void init(int threads) {
        min = new double[threads];
        answer = new ArrayList[threads];
    }

    public void run() {
        int start = (n / tn) * pass_flag;
        int end = (n * (pass_flag + 1)) / tn;

        for (int g = start; g < end; g++) {
            if (g != isp) {
                ArrayList<Double> temp = cost(n, g, sp, n, pass_flag);
                if (temp == null || temp.isEmpty()) continue;
                double var = temp.get(0) + dist(p.get(g), sp);
                if (var < min[pass_flag]) {
                    min[pass_flag] = var;
                    answer[pass_flag] = temp;
                }
            }
        }
    }

    static double dist(Point x, Point y) {
        if (x == null || y == null)
            throw new IllegalArgumentException("One of the points is null in dist()");
        double sumsq = Math.pow(x.x_c - y.x_c, 2) + Math.pow(x.y_c - y.y_c, 2);
        return Math.sqrt(sumsq);
    }

    static ArrayList<Double> cost(int S, int ini, Point sp, int n, int t_flag) {
        StringBuilder dpBuilder = new StringBuilder(S + "#" + ini);
        for (int i = 0; i < n; i++) {
            dpBuilder.append("#").append(p.get(i).thread_flag[t_flag]);
        }
        String dp = dpBuilder.toString();

        if (map.containsKey(dp))
            return new ArrayList<>(map.get(dp));

        if (S == 2) {
            ArrayList<Double> base = new ArrayList<>();
            base.add(dist(sp, p.get(ini)));
            base.add((double) ini);
            return base;
        }

        p.get(ini).thread_flag[t_flag] = false;

        double localMin = Double.MAX_VALUE;
        ArrayList<Double> bestPath = new ArrayList<>();

        for (int ik = 0; ik < n; ik++) {
            if (p.get(ik).thread_flag[t_flag]) {
                ArrayList<Double> ret = cost(S - 1, ik, sp, n, t_flag);
                if (ret == null || ret.isEmpty()) continue;
                double var = ret.get(0) + dist(p.get(ik), p.get(ini));
                if (var < localMin) {
                    localMin = var;
                    bestPath = new ArrayList<>(ret); // deep copy
                }
            }
        }

        p.get(ini).thread_flag[t_flag] = true;

        if (!bestPath.isEmpty()) {
            bestPath.set(0, localMin);
            bestPath.add((double) ini);
        } else {
            bestPath.add(localMin);
            bestPath.add((double) ini);
        }

        map.put(dp, new ArrayList<>(bestPath));
        return bestPath;
    }
}
