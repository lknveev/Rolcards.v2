package com.adri1711.rolcards.systems;

import com.adri1711.rolcards.RolCards;
import com.adri1711.rolcards.cards.Card;
import com.adri1711.rolcards.cards.CardClass;
import com.adri1711.rolcards.jugador.Jugador;
import com.adri1711.rolcards.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import java.util.*;

/** Stage 6-12 gameplay/admin command layer. Uses existing RolCards ownership via Vault permissions. */
public final class CardSystems {
    private CardSystems() {}
    private static final Random RANDOM = new Random();

    public static boolean handleCommand(RolCards plugin, CommandSender sender, Command command, String label, String[] a) {
        if (!command.getName().equalsIgnoreCase("rolcards")) return false;
        if (a.length == 0) return false;
        String sub=a[0].toLowerCase();
        if (!(sender instanceof Player)) return false;
        Player p=(Player)sender;
        if (sub.equals("class") || sub.equals("classes")) { classCommand(plugin,p,a); return true; }
        if (sub.equals("deck")) { deckCommand(plugin,p,a); return true; }
        if (sub.equals("collection") || sub.equals("cards")) { collection(plugin,p,a); return true; }
        if (sub.equals("pack")) { pack(plugin,p,a); return true; }
        if (sub.equals("rank") || sub.equals("history")) { rankHistory(plugin,p,a); return true; }
        if (sub.equals("card") && a.length>=2 && a[1].equalsIgnoreCase("give")) { give(plugin,p,a); return true; }
        return false;
    }
    private static void classCommand(RolCards pl, Player p, String[] a) {
        if (a.length<2) { p.sendMessage(ChatColor.YELLOW+"Classes: 0 Normal, 1 Hunter, 2 Mage, 3 Warrior, 4 Assassin, 5 Paladin, 6 Necromancer, 7 Druid"); return; }
        CardClass c=parseClass(a[1]); if(c==null){p.sendMessage(ChatColor.RED+"Unknown class.");return;}
        if(!p.hasPermission("rolcards.class."+c.name().toLowerCase())){p.sendMessage(ChatColor.RED+"You do not have permission for this class.");return;}
        Jugador j=Utils.buscaJugador(p,pl); if(j==null){p.sendMessage(ChatColor.RED+"Player data is not loaded.");return;}
        j.setClase(c); p.sendMessage(ChatColor.GREEN+"Class set to "+c.name());
    }
    private static void deckCommand(RolCards pl, Player p, String[] a) {
        Jugador j=Utils.buscaJugador(p,pl); if(j==null)return;
        if(a.length<2 || a[1].equalsIgnoreCase("list")) {
            p.sendMessage(ChatColor.GREEN+"Deck: "+j.getCartas().size()+"/"+pl.getDeckSize());
            for(Card c:j.getCartas()) p.sendMessage(ChatColor.GRAY+"- "+ChatColor.stripColor(c.getCardName()));
            return;
        }
        if(a[1].equalsIgnoreCase("clear")){j.getCartas().clear();j.getCartasCopia().clear();p.sendMessage(ChatColor.YELLOW+"Deck cleared.");return;}
        if(a[1].equalsIgnoreCase("add") && a.length>=3){
            Card c=find(pl,a[2]); if(c==null){p.sendMessage(ChatColor.RED+"Card not found.");return;}
            if(!owned(p,c)){p.sendMessage(ChatColor.RED+"You do not own that card.");return;}
            j.addCarta(c); p.sendMessage(ChatColor.GREEN+"Added "+c.getCardName()); return;
        }
        if(a[1].equalsIgnoreCase("remove") && a.length>=3){Card c=find(pl,a[2]);if(c!=null){j.removeCard(c);p.sendMessage(ChatColor.GREEN+"Removed "+c.getCardName());}return;}
        p.sendMessage(ChatColor.YELLOW+"Usage: /rolcards deck [list|clear|add <card>|remove <card>]");
    }
    private static void collection(RolCards pl, Player p, String[] a) {
        CardClass cc=null; if(a.length>=2)cc=parseClass(a[1]);
        int n=0; for(Card c:pl.getCartas()){if(cc!=null&&c.getClase()!=cc)continue;if(owned(p,c)){p.sendMessage(ChatColor.GRAY+"["+c.getClase()+"] "+ChatColor.stripColor(c.getCardName())+" $"+c.getCardPrice()+" / mana "+c.getCardCost());n++;}}
        p.sendMessage(ChatColor.GREEN+"Collection entries: "+n);
    }
    private static void pack(RolCards pl, Player p, String[] a) {
        Economy e=RolCards.getEconomy(); double price=a.length>=2?parseDouble(a[1],5000):5000;
        if(e==null){p.sendMessage(ChatColor.RED+"Vault economy is required for packs.");return;}
        if(e.getBalance((OfflinePlayer)p)<price){p.sendMessage(ChatColor.RED+"Not enough money.");return;}
        e.withdrawPlayer((OfflinePlayer)p,price);
        for(int i=0;i<3;i++){Card c=pl.getCartas().get(RANDOM.nextInt(pl.getCartas().size()));pl.addPerm(c.getCardPermission(),p);p.sendMessage(ChatColor.GOLD+"Pack card: "+ChatColor.stripColor(c.getCardName()));}
    }
    private static void rankHistory(RolCards pl, Player p, String[] a) {
        if(a[0].equalsIgnoreCase("rank")){p.sendMessage(ChatColor.AQUA+"Rating: "+pl.getElo1(p.getName()));return;}
        p.sendMessage(ChatColor.AQUA+"Match history is stored by the server's existing Elo/stats system. Current kills: "+pl.getKills1(p.getName())+" deaths: "+pl.getDeaths1(p.getName()));
    }
    private static void give(RolCards pl, Player p, String[] a){
        if(!p.hasPermission("rolcards.admin.card.give")){p.sendMessage(ChatColor.RED+"No permission.");return;}
        if(a.length<3){p.sendMessage(ChatColor.YELLOW+"Usage: /rolcards card give <card> [player]");return;}
        Card c=find(pl,a[2]);if(c==null){p.sendMessage(ChatColor.RED+"Card not found.");return;}
        Player target=p;if(a.length>=4&&p.hasPermission("rolcards.admin")){Player t=pl.getServer().getPlayer(a[3]);if(t!=null)target=t;}
        pl.addPerm(c.getCardPermission(),target);p.sendMessage(ChatColor.GREEN+"Granted "+c.getCardName()+" to "+target.getName());
    }
    private static boolean owned(Player p,Card c){return c.getCardPermission()==null||c.getCardPermission().isEmpty()||p.hasPermission(c.getCardPermission());}
    private static Card find(RolCards p,String name){for(Card c:p.getCartas())if(ChatColor.stripColor(c.getCardName()).equalsIgnoreCase(name)||c.getCardName().equalsIgnoreCase(name))return c;return null;}
    private static CardClass parseClass(String s){try{int i=Integer.parseInt(s);return CardClass.values()[i];}catch(Exception ignored){}try{return CardClass.valueOf(s.toUpperCase());}catch(Exception e){return null;}}
    private static double parseDouble(String s,double d){try{return Double.parseDouble(s);}catch(Exception e){return d;}}
}
