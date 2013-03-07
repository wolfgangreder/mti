/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import java.math.BigDecimal;

/**
 * Ein gelagertes Ersatzteil
 *
 * @author wolfi
 */
public interface SparePart extends InventoryObject
{

  public static interface Builder<S extends SparePart> extends InventoryObject.Builder<S>,
                                                               BaseBuilder<S>
  {

    /**
     * Kopiert die Werte von {@code part}
     *
     * @param part
     * @return {
     * @this this}
     * @throws NullPointerException wenn {@code part==null}
     */
    public Builder<? extends SparePart> copy(SparePart part) throws NullPointerException;

    /**
     * Die vorrätige Menge des Ersatzteils.
     *
     * @param amount
     * @return {@code this}
     * @throws NullPointerException wenn {@code amount==null}
     */
    public Builder<? extends SparePart> amount(BigDecimal amount) throws NullPointerException;

    @Override
    public S build() throws IllegalArgumentException;

  }

  public static interface BuilderFactory
  {

    public SparePart.Builder<? extends SparePart> createBuilder();

  }

  /**
   * Die vorrätige Menge
   *
   * @return niemals {@code null}
   */
  public BigDecimal getAmount();

}
