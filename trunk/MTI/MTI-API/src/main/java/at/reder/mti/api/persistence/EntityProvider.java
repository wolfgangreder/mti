/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence;

import java.util.Collection;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 * @param <K> Key-Class
 * @param <E> Entity-Class
 */
public interface EntityProvider<K, E> extends Lookup.Provider
{

  /**
   * Liefert das Entity mit dem Key {@code key}. Wenn {@code key==null} wird {@code null} zurück gegeben.
   *
   * @param key
   * @return Entity mit dem Key {@code key} oder {@code null} falls das Entity nicht gefunden werden kann.
   * @throws ProviderException
   */
  public E get(K key) throws ProviderException;

  /**
   * Liefert die Entities mit den Keys {@code keys}. Für nicht gefundene Keys wird nichts geliefert (Die Liste enthält niemals
   * {@code null}.
   *
   * @param keys Die Keys die gesucht werden sollen.
   * @return Eine Liste mit den gefundenen Entities (niemals {@code null}).
   * @throws ProviderException
   */
  public List<E> get(Collection<? extends K> keys) throws ProviderException;

  /**
   * Speichert das Entity {@code entity}.
   *
   * @param entity Das zu speichernde Enitity. Darf nicht null sein
   * @throws ProviderException
   */
  public void store(E entity) throws ProviderException;

  /**
   * Speichert die Entitites {@code entities}
   *
   * @param entities Collection mit den Entities. {@code null}-Einträge werden ignoriert.
   * @throws ProviderException
   */
  public void store(Collection<? extends E> entities) throws ProviderException;

  /**
   * Löscht die Keys {@code keys} aus der Datenbank.
   *
   * @param keys Collection mit den Keys. {@code null}-Einträge werden ignoriert.
   * @throws ProviderException
   */
  public void delete(Collection<? extends K> keys) throws ProviderException;

  /**
   * Löscht den Key {@code key}. Wenn {@code key==null} wird nichts gemacht.
   *
   * @param key
   * @throws ProviderException
   */
  public void delete(K key) throws ProviderException;

}
