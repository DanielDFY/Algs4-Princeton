import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int height;
    private int width;

    public SeamCarver(Picture pic) {
        if (pic == null)
            throw new IllegalArgumentException();
        picture = new Picture(pic);
        width = picture.width();
        height = picture.height();
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int col, int row) {
        if (col < 0 || row < 0 || col >= width || row >= height)
            throw new IllegalArgumentException();
        if (col == 0 || row == 0 || col == width - 1 || row == height - 1) {
            return 1000;
        } else {
            int up, down, left, right;
            left = picture.getRGB(col - 1, row);
            right = picture.getRGB(col + 1, row);
            up = picture.getRGB(col, row - 1);
            down = picture.getRGB(col, row + 1);
            return Math.sqrt(deltaX(left, right) + deltaY(up, down));
        }
    }

    public int[] findVerticalSeam() {
        double[][] energy = new double[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                energy[i][j] = energy(j, i);
            }
        }
        return findSP(width, height, energy);
    }

    public int[] findHorizontalSeam() {
        double[][] energy = new double[width][height];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                energy[j][i] = energy(j, i);
            }
        }
        return findSP(height, width, energy);
    }

    public void removeVerticalSeam(int[] seam) {
        if (width <= 1 || !isValidSeam(seam, width, height))
            throw new IllegalArgumentException();

        --width;
        Picture newPic = new Picture(width, height);

        for (int row = 0; row < height; ++row)
            for (int col = 0; col < width; ++col) {

                if (col < seam[row])
                    newPic.set(col, row, picture.get(col, row));
                else
                    newPic.set(col, row, picture.get(col + 1, row));

            }
        picture = new Picture(newPic);
    }

    public void removeHorizontalSeam(int[] seam) {
        if (height <= 1 || !isValidSeam(seam, height, width))
            throw new IllegalArgumentException();

        --height;
        Picture newPic = new Picture(width, height);

        for (int row = 0; row < height; ++row)
            for (int col = 0; col < width; ++col) {

                if (row < seam[col])
                    newPic.set(col, row, picture.get(col, row));
                else
                    newPic.set(col, row, picture.get(col, row + 1));

            }
        picture = new Picture(newPic);
    }

    private int getR(int c) { return (c >> 16) & 0xFF; }

    private int getG(int c) {
        return (c >> 8) & 0xFF;
    }

    private int getB(int c) {
        return c & 0xFF;
    }

    private double deltaX(int left, int right) {
        int rDiff = getR(right) - getR(left);
        int gDiff = getG(right) - getG(left);
        int bDiff = getB(right) - getB(left);
        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    private double deltaY(int up, int down) {
        int rDiff = getR(down) - getR(up);
        int gDiff = getG(down) - getG(up);
        int bDiff = getB(down) - getB(up);
        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    private void relax(int from, int to, double e, int[] seamPath, double[] seamDist) {
        if (seamDist[to] > seamDist[from] + e) {
            seamPath[to] = from;
            seamDist[to] = seamDist[from] + e;
        }
    }

    private int[] findSP(int w, int h, double[][] energy) {
        int n = w * h;
        int[] seamPath = new int[n + 1];
        double[] seamDist = new double[n + 1];
        int[] seam = new int[h];

        for (int i = 0; i < w; ++i) {
            seamPath[i] = -1;
            seamDist[i] = energy[0][i];
        }

        for (int i = w; i < n + 1; ++i)
            seamDist[i] = Double.POSITIVE_INFINITY;

        for (int i = w; i < n; ++i) {
            int x = i / w;
            int y = i % w;
            if (y > 0) relax(i - w - 1, i, energy[x][y], seamPath, seamDist);
            if (y < w - 1) relax(i - w + 1, i, energy[x][y], seamPath, seamDist);
            relax(i - w, i, energy[x][y], seamPath, seamDist);
        }

        for (int i = n - w; i < n; ++i)
            relax(i, n, 0, seamPath, seamDist);

        for (int v = seamPath[n]; v != -1; v = seamPath[v])
            seam[v / w] = v % w;

        return seam;
    }

    private boolean isValidSeam(int[] seam, int w, int h) {
        if (seam ==  null || seam.length != h)
            return false;
        for (int i = 0; i < h - 1; ++i) {
            if (seam[i] < 0 || seam[i] >= w || Math.abs(seam[i] - seam[i + 1]) > 1) {
                return false;
            }
        }
        return !(seam[h - 1] < 0 || seam[h - 1] >= w);
    }
}
