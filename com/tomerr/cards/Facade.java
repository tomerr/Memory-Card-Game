//Cards Memory Game by Tomer Robinson (tomerr@tomerr.com).

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
