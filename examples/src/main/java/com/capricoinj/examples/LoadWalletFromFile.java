/**
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.capricoinj.examples;

import com.capricoinj.core.Wallet;

import java.io.File;

/**
 * LoadWalletFromFile loads a serialized wallet and prints information about what it contains.
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH CAPRICOIN
public class LoadWalletFromFile {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java LoadWalletFromFile <filename>");
            return;
        }

        Wallet wallet = Wallet.loadFromFile(new File(args[0]));
        System.out.println(wallet.toString());
    }
}
