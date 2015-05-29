package org.veggeberg.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType
{
    HAS_ALBUM, HAS_TRACK, HAS_GENRE
}
