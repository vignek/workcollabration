import java.util.*;

public class DayEight {


	public static void main (String [] args) {
		Scanner in = new Scanner(System.in);
		Map<String,Integer> One = new HashMap<String,Integer>();
		int N=in.nextInt();
		in.nextLine();
		
		for(int i=0;i<N;i++)
		{
			String name=in.nextLine();
			int phone=in.nextInt();
			One.put(name, phone);
			in.nextLine();
		}

		while(in.hasNext())
		{
			String s=in.nextLine();
			if(One.containsKey(s)){
				System.out.println(s+"="+One.get(s));
			}
			else{
				System.out.println("not found");
			}
		}

	
	}

}
