package com.example.lottery2;

import java.util.Arrays;

/**
 * TODO: 添加号码检验。不过作为个人使用，添不添加无所谓
 */
public abstract class Lottery {

    public final int[][] results;
    public final int multiple;

    public Lottery(int count, int multiple) {
        this.results = new int[count][7];
        this.multiple = multiple;
    }

    /**
     * 单式票
     */
    public static class SingleLottery extends Lottery {
        public SingleLottery(int[][] rs) {
            this(rs, 1);
        }

        public SingleLottery(int[][] rs, int multiple) {
            super(rs.length, multiple);
            for (int i = 0; i < rs.length; i++) {
                int[] src = rs[i];
                System.arraycopy(src, 0, this.results[i], 0, 7);
            }
        }
    }

    /**
     * 复式票
     */
    public static class CombLottery extends Lottery {
        private final int[] pre;
        private final int[] suf;

        public CombLottery(int[] pre, int[] suf) {
            this(pre, suf, 1);
        }

        public CombLottery(int[] pre, int[] suf, int multiple) {
            super(Lottery0Utils.comb(pre.length, 6) * suf.length, multiple);
            Arrays.sort(pre);
            Arrays.sort(suf);
            this.pre = pre;
            this.suf = suf;
            fillResults();
        }

        private void fillResults() {
            int k = 0;
            for (int i1 = 0; i1 <= pre.length - 6; i1++) {
                for (int i2 = i1 + 1; i2 <= pre.length - 5; i2++) {
                    for (int i3 = i2 + 1; i3 <= pre.length - 4; i3++) {
                        for (int i4 = i3 + 1; i4 <= pre.length - 3; i4++) {
                            for (int i5 = i4 + 1; i5 <= pre.length - 2; i5++) {
                                for (int i6 = i5 + 1; i6 <= pre.length - 1; i6++) {
                                    for (int j = 0; j <= suf.length - 1; j++) {
                                        results[k][0] = pre[i1];
                                        results[k][1] = pre[i2];
                                        results[k][2] = pre[i3];
                                        results[k][3] = pre[i4];
                                        results[k][4] = pre[i5];
                                        results[k][5] = pre[i6];
                                        results[k][6] = suf[j];
                                        k += 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // for end
        }
    }

    /**
     * 胆拖票
     */
    public static class FixUnfixLottery extends Lottery {
        private final int[] preFix;
        private final int[] preUnfix;
        private final int[] suf;

        public FixUnfixLottery(int[] preFix, int[] preUnfix, int[] suf) {
            this(preFix, preUnfix, suf, 1);
        }

        public FixUnfixLottery(int[] preFix, int[] preUnfix, int[] suf, int multiple) {
            super(Lottery0Utils.comb(preUnfix.length, 6 - preFix.length) * suf.length, multiple);
            this.preFix = preFix;
            this.preUnfix = preUnfix;
            this.suf = suf;

            fillResults();
        }

//        /**
//         * 递归 fill，从 nBalls 中选择 mBalls 个球（其中 n、m 表示组合数 C(n, m) 中的符号）
//         */
//        private void fill(int[] nBalls, int[] mBalls, int nStart, int mPos) {
//            if (mPos == mBalls.length) {
//                // TODO: use the mBalls
//            } else {
//                for (int i = nStart; i < nBalls.length - mBalls.length + mPos; i++) {
//                    mBalls[mPos] = nBalls[i];
//                    fill(nBalls, mBalls, i + 1, mPos + 1);
//                }
//            }
//        }

        private int[] preMBalls;
        private int fillPos;

        private void fillResults() {
            preMBalls = new int[6 - preFix.length];
            fillPos = 0;
            fillPreM(0, 0);
        }

        private void fillPreM(int nStart, int mPos) {
            if (mPos == preMBalls.length) {
                whenPrefillOne();
            } else {
                for (int i = nStart; i < preUnfix.length - preMBalls.length + mPos + 1; i++) {
                    preMBalls[mPos] = preUnfix[i];
                    fillPreM(i + 1, mPos + 1);
                }
            }
        }

        private void whenPrefillOne() {
            for (int s : suf) {
                int[] target = results[fillPos];

                System.arraycopy(preFix, 0, target, 0, preFix.length);
                System.arraycopy(preMBalls, 0, target, preFix.length, preMBalls.length);
                Arrays.sort(target, 0, 6);

                target[6] = s;

                fillPos += 1;
            }
        }
    }

}
