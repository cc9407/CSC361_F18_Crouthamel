package com.packtpub.mygdx.retromario.util;

import com.badlogic.gdx.graphics.Color;

public enum CharacterSkin {

	//instance variablesS
		WHITE("White", 1.0f, 1.0f, 1.0f), GRAY("Gray", 0.7f, 0.7f, 0.7f), BROWN("Brown", 0.7f, 0.5f, 0.3f);
		private String name;
		private Color color = new Color();

		/*
		 * constructor
		 */
		private CharacterSkin(String name, float r, float g, float b) {
			this.name = name;
			color.set(r, g, b, 1.0f);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 * getter for the name 
		 */
		@Override
		public String toString() {
			return name;
		}

		/*
		 * getter for color
		 */
		public Color getColor() {
			return color;
		}
}