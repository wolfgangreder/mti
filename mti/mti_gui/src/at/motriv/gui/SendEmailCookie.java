/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui;

import javax.mail.Address;
import org.openide.nodes.Node.Cookie;

/**
 *
 * @author wolfi
 */
public interface SendEmailCookie extends Cookie
{

  public void sendEmail(Address from,String subject, String text);
}
