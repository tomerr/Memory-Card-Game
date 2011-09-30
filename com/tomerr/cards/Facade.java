//Cards Memory Game by Tomer Robinson (21755699) and Saggi Brener (17257643).

package com.tomerr.cards;
//Controller
class Facade {
	
	View view = View.getInstance();
	Model model = Model.getInstance();

	public void doView() {
		view.init();
		view.setVisible(true);
	}
	public void doModel() {
		model.init();
	}
	public static void main(String args[]) {
		Facade facade = new Facade();
		facade.doModel();
		facade.doView();
	}
}
