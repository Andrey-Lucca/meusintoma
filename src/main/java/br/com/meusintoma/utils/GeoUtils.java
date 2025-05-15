package br.com.meusintoma.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class GeoUtils {

    public static Point createPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);
        return point;
    }
}
