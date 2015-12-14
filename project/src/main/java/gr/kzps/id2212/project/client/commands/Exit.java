package gr.kzps.id2212.project.client.commands;

public class Exit extends CommandAbstr {

	@Override
	public void execute() {
		console.print("Bye!");
		console.halt();
	}

}
