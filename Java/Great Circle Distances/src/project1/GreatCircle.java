/*
 * Author: Jason Fisher, jfisher2009@my.fit.edu
 * Course: CSE 4051, Fall 2013
 * Project proj1, Great Circle Distances
 */

package project1;

import java.util.ArrayList;
import java.util.Scanner;

public class GreatCircle {

	private static final int commaNeeded = 1000;
	private static final double earthRadius = 6371.01;


	public static ArrayList<Double []> getUserInput () {
		ArrayList<Double []> locations = new ArrayList<Double []> ();
		final Scanner stdInput = new Scanner (System.in);
		int lineCounter = 0;
		if (stdInput.hasNext()) {
			while (stdInput.hasNextDouble()) {
				final Scanner lineReader =
						new Scanner (stdInput.nextLine());
				lineCounter++;
				final Double [] location = new Double [2];
				location [0] = lineReader.nextDouble();
				location [1] = lineReader.nextDouble();
				locations.add(location);
				lineReader.close();
			}
		}
		stdInput.close();
		if (lineCounter == 0) {
			System.out.println("No lines of input.");
		}
		if (lineCounter == 1) {
			System.out.println("One line of input.");
		}

		return locations;
	}

	public static void calculateDistances (final ArrayList<Double []> locations) {
		for (int i = 0; i < locations.size() - 1; i++) {
			final Double [] locationP = locations.get(i);
			final Double [] locationQ = locations.get(i + 1);
			final double cosLatP = Math.cos(Math.toRadians(locationP[0]));
			final double sinLatP = Math.sin(Math.toRadians(locationP[0]));
			final double cosLatQ = Math.cos(Math.toRadians(locationQ[0]));
			final double sinLatQ = Math.sin(Math.toRadians(locationQ[0]));
			final double cosDiff = Math.cos(Math.toRadians(locationP[1] - locationQ[1]));
			final double cosT = cosLatP * cosLatQ * cosDiff
					+ sinLatP * sinLatQ;
			final int distance = (int) Math.round(
					earthRadius * Math.acos(cosT));
			if ((distance / commaNeeded) >= 1) {
				System.out.println((distance / commaNeeded)
						+ "," + (distance % commaNeeded) + " km");
			}
			else {
				System.out.println(distance + " km");
			}
		}
	}

	public static void main(final String [] args) {
		final ArrayList <Double []> locations = getUserInput();
		if (locations.size() > 1) {
			calculateDistances(locations);
		}
	}

}
