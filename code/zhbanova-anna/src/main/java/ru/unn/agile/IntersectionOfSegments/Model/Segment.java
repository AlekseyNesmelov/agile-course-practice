package ru.unn.agile.IntersectionOfSegments;

enum TypeOfIntersection {
    NotIntersection, IntersectionInOnePoint, OnePartOfOther, SegmentsHaveCommonPart
}

public class Segment {
    private Point start;
    private Point finish;

    Segment(final Point start, final Point finish) {
        this.start = start;
        this.finish = finish;
    }

    public void setFinish(final Point finish) {
        this.finish = finish;
    }

    public void setStart(final Point start) {
        this.start = start;
    }

    public Point getStart() {
        return start;
    }

    public Point getFinish() {
        return finish;
    }

    public double getStartX() {
        return start.getX();
    }

    public double getFinishX() {
        return finish.getX();
    }

    public double getStartY() {
        return start.getY();
    }

    public double getFinishY() {
        return finish.getY();
    }

    public double getLengthSegment() {
        return calculationLengthOfSegment();
    }

    @Override
    public int hashCode() {
        final int prime = 21;
        int result = 1;
        result = prime * result + start.hashCode();
        result = prime * result + finish.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object) {
        Segment segment = (Segment) object;
        return this.getStart() == segment.getStart() && this.getFinish() == segment.getFinish();
    }

    public Intersection isIntersectedWith(final Segment segment) {
        return findIntersection(this, segment);
    }

    private Intersection findIntersection(final Segment segment1, final Segment segment2) {
        TypeOfIntersection typeOfIntersection;
        Point startPointOfIntersection;
        Point finishPointOfIntersection;

        if (segment1.isIntersection(segment2)) {
            double slope1 = calculateSlope(segment1);
            double slope2 = calculateSlope(segment2);

            double b1 = segment1.getStartY() - slope1 * segment1.getStartX();
            double b2 = segment2.getStartY() - slope2 * segment2.getStartX();

            if (Math.abs(slope1 - slope2) < Double.MIN_VALUE) {
                //segment x = const convert to y = const
                if (slope1 == Double.MAX_VALUE) {
                    swapXY(segment1);
                    swapXY(segment2);
                }
                makeFirstPointWithLessX(segment1);
                makeFirstPointWithLessX(segment2);
                if (isFirstSegmentPartOfSecondWhenSlopeSame(segment2, segment1)) {
                    typeOfIntersection = TypeOfIntersection.OnePartOfOther;
                    startPointOfIntersection = segment2.getStart();
                    finishPointOfIntersection = segment2.getFinish();
                } else if (isFirstSegmentPartOfSecondWhenSlopeSame(segment1, segment2)) {
                    typeOfIntersection = TypeOfIntersection.OnePartOfOther;
                    startPointOfIntersection = segment1.getStart();
                    finishPointOfIntersection = segment1.getFinish();
                } else if (isEndOfFirstSegmentIsBeginningOfSecondWhenSlopeSame(
                        segment1, segment2)) {
                    typeOfIntersection = TypeOfIntersection.SegmentsHaveCommonPart;
                    startPointOfIntersection = segment2.getStart();
                    finishPointOfIntersection = segment1.getFinish();
                } else {
                    typeOfIntersection = TypeOfIntersection.SegmentsHaveCommonPart;
                    startPointOfIntersection = segment1.getStart();
                    finishPointOfIntersection = segment2.getFinish();
                }
                if (slope1 == Double.MAX_VALUE) {
                    startPointOfIntersection.swapXY();
                    finishPointOfIntersection.swapXY();
                }
            } else {
                typeOfIntersection = TypeOfIntersection.IntersectionInOnePoint;
                if (slope1 == Double.MAX_VALUE) {
                    double x = segment1.getStartX();
                    double y = slope2 * x + b2;
                    startPointOfIntersection = new Point(x, y);
                    finishPointOfIntersection = new Point(x, y);
                } else if (slope2 == Double.MAX_VALUE) {
                    double x = segment2.getStartX();
                    double y = slope1 * x + b1;
                    startPointOfIntersection = new Point(x, y);
                    finishPointOfIntersection = new Point(x, y);
                } else {
                    double x = (b2 - b1) / (slope1 - slope2);
                    double y = slope1 * x + b1;
                    startPointOfIntersection = new Point(x, y);
                    finishPointOfIntersection = new Point(x, y);
                }
            }
        } else {
            typeOfIntersection = TypeOfIntersection.NotIntersection;
            startPointOfIntersection = null;
            finishPointOfIntersection = null;
        }
        return new Intersection(typeOfIntersection,
                new Segment(startPointOfIntersection, finishPointOfIntersection));
    }

    private boolean isFirstSegmentPartOfSecondWhenSlopeSame(final Segment segment1,
                                                            final Segment segment2) {
        return segment1.getStartX() > segment2.getStartX()
                && segment1.getFinishX() < segment2.getFinishX();
    }

    private  boolean isEndOfFirstSegmentIsBeginningOfSecondWhenSlopeSame(final Segment segment1,
                                                                         final Segment segment2) {
        return segment1.getFinishX() > segment2.getStartX();
    }

    private boolean isIntersection(final Segment segment) {
        return isEndsOfSecondSegmentOnDifferentSidesOfFirstSegment(this, segment)
                && isEndsOfSecondSegmentOnDifferentSidesOfFirstSegment(segment, this);
    }

    private boolean isEndsOfSecondSegmentOnDifferentSidesOfFirstSegment(final Segment segment1,
                                                                         final Segment segnent2) {
        return multiplicateVectorsWithSameStart(segment1.getStart(),
                segment1.getFinish(), segnent2.getStart())
                * multiplicateVectorsWithSameStart(segment1.getStart(),
                segment1.getFinish(), segnent2.getFinish()) <= 0;
    }

    private double multiplicateVectorsWithSameStart(final Point p1,
                                                    final Point p2, final Point p3) {
        return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY())
                - (p1.getY() - p3.getY()) * (p2.getX() - p3.getX());
    }

    private double calculateSlope(final Segment segment) {
        if (segment.getFinishX() == segment.getStartX()) {
            return Double.MAX_VALUE;
        }
        return (segment.getFinishY() - segment.getStartY())
                / (segment.getFinishX() - segment.getStartX());
    }

    private void makeFirstPointWithLessX(final Segment segment) {
        if (segment.getStartX() >= segment.getFinishX()) {
            swapPoints(segment.getStart(), segment.getFinish());
        }
    }

    private void swapPoints(final Point p1,  final Point p2) {
        Point tmp = new Point(p1);
        p1.set(p2);
        p2.set(tmp);
    }

    private double calculationLengthOfSegment() {
        if (start == null || finish == null) {
            return -1.0;
        }
        return Math.sqrt(Math.pow(start.getX() - finish.getX(), 2)
                + Math.pow(start.getY() - finish.getY(), 2));
    }

    private void swapXY(final Segment segment) {
        segment.getStart().swapXY();
        segment.getFinish().swapXY();
    }
}
