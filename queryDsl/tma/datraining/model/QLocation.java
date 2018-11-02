package tma.datraining.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLocation is a Querydsl query type for Location
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLocation extends EntityPathBase<Location> {

    private static final long serialVersionUID = -935352821L;

    public static final QLocation location = new QLocation("location");

    public final StringPath city = createString("city");

    public final StringPath country = createString("country");

    public final DateTimePath<java.sql.Timestamp> createAt = createDateTime("createAt", java.sql.Timestamp.class);

    public final ComparablePath<java.util.UUID> locationId = createComparable("locationId", java.util.UUID.class);

    public final DateTimePath<java.sql.Timestamp> modifiedAt = createDateTime("modifiedAt", java.sql.Timestamp.class);

    public final SetPath<Sales, QSales> sales = this.<Sales, QSales>createSet("sales", Sales.class, QSales.class, PathInits.DIRECT2);

    public QLocation(String variable) {
        super(Location.class, forVariable(variable));
    }

    public QLocation(Path<? extends Location> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLocation(PathMetadata metadata) {
        super(Location.class, metadata);
    }

}

