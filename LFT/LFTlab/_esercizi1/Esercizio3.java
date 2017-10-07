public class Esercizio3
{
  public static boolean scan ( String s )
  {
    int state = 10;
    int i = 0;
    while ( state >= 0 && i < s . length ()) {
      final char ch = s . charAt ( i ++);
      switch ( state ) {
		case 10:
		  if(ch == ' '){
		    state = 0;
		    System.out.println("Test vuoto");
		}
		  else if ( ch >= '0' && ch <= '9'){
			state = 2;
			System.out.println("Test cifra");}
		  else if ( ch == '+' || ch == '-' )
			state = 1;
		  else if ( ch == '.')
			state = 3;
		  else
			state = -1;
          break ;
		case 0:
          if ( ch >= '0' && ch <= '9')
            state = 2;
          else if ( ch == '+' || ch == '-' )
            state = 1;
          else if ( ch == '.')
            state = 3;
          else
            state = -1;
          break ;
        case 1:
          if ( ch >= '0' && ch <= '9')
            state = 2;
          else if ( ch == '.')
            state = 3;
          else
            state = -1;
          break ;
        case 2:
          if ( ch == '.')
            state = 3;
          else if ( ch == 'e')
            state = 5;
          else if ( ch == 032)
            state = 8;
          else if (!( ch >= '0' && ch <= '9'))
            state = -1;
          break ;
        case 3:
          if ( ch >= '0' && ch <= '9')
            state = 4;
          else
            state = -1;
          break ;
        case 4:
		  if ( ch == 'e')
		    state = 5;
		  else if ( ch == 032)
		    state = 8;
		  else if (!( ch >= '0' && ch <= '9'))
		    state = -1;
          break ;
        case 5:
		  if ( ch == '+' || ch == '-')
		  	state = 6;
		  else if ( ch >= '0' && ch <= '9')
		    state = 7;
		  else
		  	state = -1;
          break ;
        case 6:
		  if ( ch >= '0' && ch <= '9')
		  	state = 7;
		  else
		  	state = -1;
          break ;
        case 7:
          if ( ch == 032)
            state = 8;
		  else if (!( ch >= '0' && ch <= '9'))
		  	state = -1;
          break ;
        case 8:
          if( ch != 032)
            state = -1;
          break;
      }
    }
    return state == 2 || state == 4 || state == 7 || state == 8;
  }
  public static void main ( String [] args )
  {
    System . out . println ( scan ( args [0]) ? " OK " : " NOPE " );
  }
}