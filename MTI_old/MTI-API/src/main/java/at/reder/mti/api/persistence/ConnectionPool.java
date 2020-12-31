/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author wolfi
 */
public interface ConnectionPool
{

  public Connection getConnection() throws SQLException;

}
