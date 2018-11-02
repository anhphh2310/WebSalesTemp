package tma.datraining.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTime is a Querydsl query type for Time
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTime extends EntityPathBase<Time> {

    private static final long serialVersionUID = 195228707L;

    public static final QTime time = new QTime("time");

    public final DateTimePath<java.sql.Timestamp> createAt = createDateTime("createAt", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> modifiedAt = createDateTime("modifiedAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Integer> quarter = createNumber("quarter", Integer.class);

    public final SetPath<Sales, QSales> sales = this.<Sales, QSales>createSet("sales", Sales.class, QSales.class, PathInits.DIRECT2);

    public final ComparablePath<java.util.UUID> timeId = createComparable("timeId", java.util.UUID.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QTime(String variable) {
        super(Time.class, forVariable(variable));
    }

    public QTime(Path<? extends Time> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTime(PathMetadata metadata) {
        super(Time.class, metadata);
    }

}

