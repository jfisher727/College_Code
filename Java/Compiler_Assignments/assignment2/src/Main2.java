
public class Main2 {

	public static void main(String[] args) {
		System.out.println("		.section	\".rodata\"");
		System.out.println("		.align		8");
		System.out.println("SC0:	.asciz		\"Hello world!\n\"");
		
		System.out.println(".set SYS_exit, 1");
		System.out.println(".macro exit_program");
		System.out.println("clr 	%o0");
		System.out.println("mov		SYS_exit, %g1");
		System.out.println("ta		0");
		System.out.println(".endm");
		
		System.out.println(".section	\".text\"");
		System.out.println(".aligh		4");
		System.out.println(".global start");
		System.out.println("start:");
		System.out.println("print SC0");
		System.out.println("exit_program");
	}

}
