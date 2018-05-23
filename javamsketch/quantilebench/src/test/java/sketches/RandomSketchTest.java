package sketches;

import data.TestDataSource;
import io.DataGrouper;
import io.SeqDataGrouper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class RandomSketchTest {
    @Test
    public void testUniformNoMergeNew() throws Exception {
        int numTrials = 1;
        int n = 20000;
        double[] data = TestDataSource.getUniform(n + 1);
        TestDataSource.shuffleArray(data);
        List<Double> ps = Arrays.asList(.1, .5, .9);
        double[] expectedQs = QuantileUtil.getTrueQuantiles(ps, data);
//        double sizeParam = 50.0;
        double[] sizeParams = {5.0, 10.0, 20.0, 50.0, 100.0};
        for (double sizeParam : sizeParams) {
            double[] average = new double[ps.size()];
            double[] average_error = new double[ps.size()];
            for (int i = 0; i < numTrials; i++) {
                RandomSketch sketch = new RandomSketch();
                sketch.setSizeParam(sizeParam);
                sketch.initialize();
                sketch.add(data);

                double[] qs = sketch.getQuantiles(ps);

                for (int j = 0; j < qs.length; j++) {
                    average[j] += qs[j];
                    average_error[j] += Math.abs(qs[j] - expectedQs[j]);
                }
            }
            for (int j = 0; j < average.length; j++) {
                average[j] /= numTrials;
                average_error[j] /= numTrials;
            }
            System.out.println(sizeParam + " " + Arrays.toString(average) + " " + Arrays.toString(average_error));
        }
        System.out.println();
    }

    @Test
    public void testUniformWithMerge() throws Exception {
        int numTrials = 100;
//        double sizeParam = 50.0;
//        double[] sizeParams = {5.0};
        double[] sizeParams = {5.0, 10.0, 20.0, 50.0, 100.0};
        int n = 20000;
        double[] data = TestDataSource.getUniform(n+1);
        TestDataSource.shuffleArray(data);

        List<Double> ps = Arrays.asList(.1, .5, .9);
        double[] expectedQs = QuantileUtil.getTrueQuantiles(ps, data);

        DataGrouper grouper = new SeqDataGrouper(200);
        ArrayList<double[]> cellData = grouper.group(data);
        for (double sizeParam : sizeParams) {
            double[] average = new double[ps.size()];
            double[] average_error = new double[ps.size()];
            for (int i = 0; i < numTrials; i++) {
                QuantileSketch mergedSketch = QuantileUtil.trainAndMerge(
                        () -> {
                            RandomSketch newSketch = new RandomSketch();
                            newSketch.setSizeParam(sizeParam);
                            return newSketch;
                        },
                        cellData
                );
                double[] qs2 = mergedSketch.getQuantiles(ps);
                for (int j = 0; j < qs2.length; j++) {
                    average[j] += qs2[j];
                    average_error[j] += Math.abs(qs2[j] - expectedQs[j]);
                }
            }
            for (int j = 0; j < average.length; j++) {
                average[j] /= numTrials;
                average_error[j] /= numTrials;
            }
            System.out.println(sizeParam + " " + Arrays.toString(average) + " " + Arrays.toString(average_error));
        }
    }

//    @Test
//    public void testFunctions() throws Exception {
//        RandomSketch sketch = new RandomSketch();
//
//        ArrayList<Double> bufferOne = new ArrayList<>();
//        ArrayList<Double> bufferTwo = new ArrayList<>();
//        ArrayList<Double> target = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            bufferOne.add((double)i);
//        }
//        for (int i = 10; i < 20; i++) {
//            bufferTwo.add((double)i);
//        }
//
//        sketch.mergeBuffers(bufferOne, bufferTwo, target);
//        System.out.println(target);
//    }

//    @Test
//    public void testUniformNoMerge() throws Exception {
//        int n = 640;
//        double[] data = TestDataSource.getUniform(n + 1);
//        TestDataSource.shuffleArray(data);
//        List<Double> ps = Arrays.asList(.1, .5, .9);
//        double[] expectedQs = QuantileUtil.getTrueQuantiles(ps, data);
//        double sizeParam = 5.0;
////        double[] sizeParams = {5.0};
//        double[] average = new double[ps.size()];
//        double[] average_error = new double[ps.size()];
//        int numTrials = 1;
//        for (int i = 0; i < numTrials; i++) {
//            RandomSketch sketch = new RandomSketch();
//            sketch.setSizeParam(sizeParam);
//            sketch.initialize();
//            sketch.add(data);
//
//            double[] qs = sketch.getQuantiles(ps);
//
//            for (int j = 0; j < qs.length; j++) {
//                average[j] += qs[j];
//                average_error[j] += Math.abs(qs[j] - expectedQs[j]);
//            }
//
////            System.out.println(Arrays.toString(qs));
////            System.out.println(sketch.getSize() / 8 + " " + sketch.b + " " + sketch.s);
////            for (int level : sketch.usedBuffers.keySet()) {
////                System.out.println(level + " " + sketch.usedBuffers.get(level).size());
////            }
////            System.out.println(sketch.activeLevel);
////            for (int level : sketch.usedBuffers.keySet()) {
////                for (ArrayList<Double> buffer : sketch.usedBuffers.get(level)) {
////                    System.out.println(buffer);
////                }
////            }
////            System.out.println(sketch.curBuffer);
//        }
//        for (int j = 0; j < average.length; j++) {
//            average[j] /= numTrials;
//            average_error[j] /= numTrials;
//        }
//        System.out.println(sizeParam + " " + Arrays.toString(average) + " " + Arrays.toString(average_error));
//
//        System.out.println();
//    }

//    @Test
//    public void testUniformWithMerge() throws Exception {
////        double sizeParam = 50.0;
//        double[] sizeParams = {5.0, 10.0, 20.0, 50.0, 100.0};
//        int n = 20000;
//        double[] data = TestDataSource.getUniform(n+1);
//
//        List<Double> ps = Arrays.asList(.1, .5, .9);
//        double[] expectedQs = QuantileUtil.getTrueQuantiles(ps, data);
//
//        DataGrouper grouper = new SeqDataGrouper(200);
//        ArrayList<double[]> cellData = grouper.group(data);
//        for (double sizeParam : sizeParams) {
//            QuantileSketch mergedSketch = QuantileUtil.trainAndMerge(
//                    () -> {
//                        RandomSketch newSketch = new RandomSketch();
//                        newSketch.setSizeParam(sizeParam);
//                        return newSketch;
//                    },
//                    cellData
//            );
//            double[] qs2 = mergedSketch.getQuantiles(ps);
//
//            System.out.println(Arrays.toString(qs2));
//            RandomSketch sketch = (RandomSketch) mergedSketch;
//            System.out.println(mergedSketch.getSize()/8 + " " + sketch.b + " " + sketch.s);
//            for (int level : sketch.usedBuffers.keySet()) {
//                System.out.println(level + " " + sketch.usedBuffers.get(level).size());
//            }
//            System.out.println(sketch.activeLevel);
//            if (sizeParam == 5.0) {
//                for (int level : sketch.usedBuffers.keySet()) {
//                    for (ArrayList<Double> buffer : sketch.usedBuffers.get(level)) {
//                        System.out.println(buffer);
//                    }
//                }
//                System.out.println(sketch.curBuffer);
//            }
////        System.out.println(((RandomSketch)mergedSketch).b);
////        System.out.println(((RandomSketch)mergedSketch).s);
//
//            assertArrayEquals(expectedQs, qs2, n / sizeParam);
//        }
//    }
}