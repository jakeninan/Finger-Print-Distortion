/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diploma;

import Jama.Matrix;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

class deserialize {

    public static void main(String[] args) {
        HashMap<Integer, Matrix> si = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("distrotions"));
            si = (HashMap<Integer, Matrix>) objectInputStream.readObject();
            objectInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(si);

    }
}
