package computation;

import java.io.*;
import java.util.*;

class AVLNode<T extends Comparable> {
  T data;
  int balance;
  ArrayList<AVLNode<T>> link;
  
  AVLNode ( T data ) {
    this.data = data;
    balance = 2;
    link = new ArrayList<AVLNode<T>> ( 2 );
    link.add ( null ); link.add ( null );
  }
}

class AVLTree {
  private static boolean new_depth;
  
  private static<T extends Comparable>
  AVLNode<T> single_rotate ( AVLNode<T> tree, int dir ) {
    int not_dir = dir == 0 ? 1 : 0;
    AVLNode<T> save = tree.link.get ( dir );
    
    tree.link.set ( dir, save.link.get ( not_dir ) );
    save.link.set ( not_dir, tree );
    
    return save;
  }
  
  private static<T extends Comparable>
  AVLNode<T> double_rotate ( AVLNode<T> tree, int dir ) {
    int not_dir = dir == 0 ? 1 : 0;
    AVLNode<T> save = tree.link.get ( dir ).link.get ( not_dir );
    
    tree.link.get ( dir ).link.set ( not_dir, save.link.get ( dir ) );
    save.link.set ( dir, tree.link.get ( dir ) );
    tree.link.set ( dir, save );
    
    save = tree.link.get ( dir );
    tree.link.set ( dir, save.link.get ( not_dir ) );
    save.link.set ( not_dir, tree );
    
    return save;
  }
  
  private static<T extends Comparable>
  void fix_balance_factors ( AVLNode<T> tree, int dir ) {
    int not_dir = dir == 0 ? 1 : 0;
    
    if ( tree.link.get ( dir ).link.get ( not_dir ).balance == dir ) {
      tree.balance = 2;
      tree.link.get ( dir ).balance = not_dir;
    }
    else if ( tree.link.get ( dir ).link.get ( not_dir ).balance == not_dir ) {
      tree.balance = dir;
      tree.link.get ( dir ).balance = 2;
    }
    else {
      tree.balance = tree.link.get ( dir ).balance = 2;
    }
    
    tree.link.get ( dir ).link.get ( not_dir ).balance = 2;
  }
  
  private static<T extends Comparable>
  AVLNode<T> rebalance_insert ( AVLNode<T> tree, int dir ) {
    int not_dir = dir == 0 ? 1 : 0;
    
    if ( tree.balance == dir ) {
      if ( tree.link.get ( dir ).balance == dir ) {
        tree.link.get ( dir ).balance = 2;
        tree.balance = 2;
        
        tree = single_rotate ( tree, dir );
      }
      else {
        fix_balance_factors ( tree, dir );
        
        tree = double_rotate ( tree, dir );
      }
      
      new_depth = false;
    }
    else if ( tree.balance == not_dir ) {
      tree.balance = 2;
      new_depth = false;
    }
    else {
      tree.balance = dir;
    }
    
    return tree;
  }
  
  private static<T extends Comparable>
  AVLNode<T> rebalance_remove ( AVLNode<T> tree, int dir ) {
    int not_dir = dir == 0 ? 1 : 0;
    
    if ( tree.balance == dir ) {
      tree.balance = 2;
    }
    else if ( tree.balance == not_dir ) {
      if ( tree.link.get ( not_dir ).balance == not_dir ) {
        tree.link.get ( not_dir ).balance = 2;
        tree.balance = 2;
        
        tree = single_rotate ( tree, not_dir );
      }
      else if ( tree.link.get ( not_dir ).balance == 2 ) {
        tree.link.get ( not_dir ).balance = dir;
        
        tree = single_rotate ( tree, not_dir );
      }
      else {
        fix_balance_factors ( tree, not_dir );
        
        tree = double_rotate ( tree, not_dir );
      }
      
      new_depth = false;
    }
    else {
      tree.balance = not_dir;
      new_depth = false;
    }
    
    return tree;
  }

  private static<T extends Comparable>
  AVLNode<T> insert_r ( AVLNode<T> tree, T data ) {
    if ( tree == null ) {
      tree = new AVLNode<T> ( data );
      new_depth = true;
    }
    else {
      int dir = data.compareTo ( tree.data ) > 0 ? 1 : 0;
      
      tree.link.set ( dir, insert ( tree.link.get ( dir ), data ) );
      
      if ( new_depth ) {
        tree = rebalance_insert ( tree, dir );
      }
    }
    
    return tree;
  }
  
  private static<T extends Comparable>
  AVLNode<T> remove_r ( AVLNode<T> tree, T data ) {
    if ( tree == null ) {
      new_depth = false;
    }
    else if ( data.compareTo ( tree.data ) == 0 ) {
      if ( tree.link.get ( 0 ) != null && tree.link.get ( 1 ) != null ) {
        AVLNode<T> heir = tree.link.get ( 0 );
        
        while ( heir.link.get ( 1 ) != null ) {
          heir = heir.link.get ( 1 );
        }
        
        tree.data = heir.data;
        
        tree.link.set ( 0, remove_r ( tree.link.get ( 0 ), tree.data ) );
        
        if ( new_depth ) {
          tree = rebalance_remove ( tree, 0 );
        }
      }
      else {
        AVLNode<T> save = tree;
        
        tree = tree.link.get ( tree.link.get ( 0 ) == null ? 1 : 0 );
        
        new_depth = true;
      }
    }
    else {
      int dir = data.compareTo ( tree.data ) > 0 ? 1 : 0;
      
      tree.link.set ( dir, remove_r ( tree.link.get ( dir ), data ) );
      
      if ( new_depth ) {
        tree = rebalance_remove ( tree, dir );
      }
    }
    
    return tree;
  }
  
  public static<T extends Comparable>
  AVLNode<T> insert ( AVLNode<T> tree, T data ) {
    new_depth = false;
    return insert_r ( tree, data );
  }
  
  public static<T extends Comparable>
  AVLNode<T> remove ( AVLNode<T> tree, T data ) {
    new_depth = false;
    return remove_r ( tree, data );
  }

  public static<T extends Comparable>
  void structure ( AVLNode<T> tree, int level ) {
    int i;
    
    if ( tree == null ) {
      for ( i = 0; i < level; i++ ) {
        System.out.print ( "\t" );
      }
      System.out.println();
      
      return;
    }
    
    structure ( tree.link.get ( 1 ), level + 1 );
    
    for ( i = 0; i < level; i++ ) {
      System.out.print ( "\t" );
    }
    System.out.println ( tree.data + "(" + tree.balance + ")" );
    
    structure ( tree.link.get ( 0 ), level + 1 );
  }
}
//
//public class Main {
//  public static void main ( String[] args ) throws IOException {
//    AVLNode<Integer> tree = null;
//    
//    for ( int i = 0; i < 10; i++ ) {
//      tree = AVLTree.insert ( tree, i );
//      
//      AVLTree.structure ( tree, 0 );
//      System.out.println ( "\n------------------------" );
//      
//      System.in.read();
//    }
//    
//    for ( int i = 0; i < 10; i++ ) {
//      tree = AVLTree.remove ( tree, i );
//      
//      AVLTree.structure ( tree, 0 );
//      System.out.println ( "\n------------------------" );
//      
//      System.in.read();
//    }
//  }
//}