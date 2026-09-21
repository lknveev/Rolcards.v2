package com.adri1711.rolcards.listeners;
import com.adri1711.rolcards.RolCards;
import com.adri1711.rolcards.cards.Card;
import com.adri1711.rolcards.cards.CardClass;
import com.adri1711.rolcards.jugador.Jugador;
import com.adri1711.rolcards.utils.Utils;
import org.bukkit.event.*; import org.bukkit.event.player.*; import org.bukkit.entity.Player;
import java.io.IOException; import java.util.*;
public class ProgressionListener implements Listener {
 private final RolCards p; public ProgressionListener(RolCards p){this.p=p;}
 @EventHandler public void join(PlayerJoinEvent e){Player x=e.getPlayer(); Jugador j=Utils.buscaJugador(x,p); if(j==null)return; String base="uuid."+x.getUniqueId();
  String cls=p.getCards().getString(base+".class"); if(cls!=null)try{j.setClase(CardClass.valueOf(cls.toUpperCase()));}catch(Exception ignored){}
  if (j.getCartas().isEmpty()) { List<String> names=p.getCards().getStringList(base+".deck"); for(String n:names){Card c=Utils.buscaCarta(n,p);if(c!=null)j.addCarta(c);} }
  if (j.getCartas().isEmpty()) { int added=0; for(Card c:p.getCartas()){ if(c.getClase()==CardClass.NORMAL){ j.addCarta(c); if(++added>=5) break; } } } }
 @EventHandler public void quit(PlayerQuitEvent e){Player x=e.getPlayer();Jugador j=Utils.buscaJugador(x,p);if(j==null)return;String base="uuid."+x.getUniqueId();p.getCards().set(base+".class",j.getClase().name().toLowerCase());List<String> deck=new ArrayList<String>();for(Card c:j.getCartasCopia())deck.add(c.getCardName());p.getCards().set(base+".deck",deck);try{p.getCards().save(p.getCardsFile());}catch(IOException ex){p.getLogger().warning("Could not save progression: "+ex.getMessage());}}
}
