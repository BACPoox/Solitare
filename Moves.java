import java.util.Stack;

public class Moves extends Rules {
/*
    Moves(){}
    Moves(Table oldTable){

        //Copy for cardsOnTable and hasCardTable
        for (int rows = 0; rows < MAX_ROWS; rows++){
            for (int cols = 0; cols < MAX_COLUMNS; cols++) {
                this.hasCardTable[rows][cols] = oldTable.hasCardTable[rows][cols];
                this.cardsOnTable[rows][cols] = new Card();
                if (oldTable.hasCardTable[rows][cols])
                    this.cardsOnTable[rows][cols].copyThisCard(oldTable.cardsOnTable[rows][cols]);
            }
        }

        //Copy for top panel
        for (int rows = 0; rows < 3; rows++){
            for (int cols = 0; cols < 7; cols++){
                this.hasCardTop[0][cols] = oldTable.hasCardTop[0][cols];
                if (cols == 1)
                    this.hasCardTop[rows][1] = oldTable.hasCardTop[rows][1];
            }
        }

        stackCopier(this.deck, oldTable.deck, oldTable.deck.size());
        if (this.deck.size() > 0 && this.deck.peek().isUp())
            this.deck.peek().Flip();

        for (int i = 0; i < 3; i++) {
            this.hasCardTop[i][1] = oldTable.hasCardTop[i][1];
            this.up[i] = null;
            this.up[i] = oldTable.up[i];
        }

        stackCopier(this.throwaway, oldTable.throwaway, oldTable.throwaway.size());
        stackCopier(this.hearts, oldTable.hearts, oldTable.hearts.size());
        stackCopier(this.diamonds, oldTable.diamonds, oldTable.diamonds.size());
        stackCopier(this.spades, oldTable.spades, oldTable.spades.size());
        stackCopier(this.clubs, oldTable.clubs, oldTable.clubs.size());

        this.setDeckSize(oldTable.getDeckSize());
        this.setDrawPileSize(oldTable.getDeckSize());
    }

    public void Draw() {
        //System.out.println("Draw function called");

        if (!deck.isEmpty()) { //IDEAL

            addToDrawPile();

            if (hasCardTop[2][1])
                throwaway.push(up[2]);

            for (int i = 0; i < 3; i++)
                hasCardTop[i][1] = i < stackSize();

            for (int i = 2; i > 0; i--)
                up[i] = up[i - 1];

            up[0] = deck.peek();
            up[0].Flip();

            deck.pop();

            if (deck.isEmpty())
                hasCardTop[0][0] = false;


        } else { //RESET

            if (getDeckSize() > 0) {

                for (int i = 0; i < stackSize(); i++) {
                    throwaway.push(up[i]);
                    up[i] = null;
                    hasCardTop[i][1] = false;
                }

                for (int i = 0; i < throwaway.size(); i++) {
                    deck.push(throwaway.peek());
                    throwaway.pop();
                    deck.peek().Flip();
                }

                if (deck.size() > 0)
                    hasCardTop[0][0] = true;

                CardPileReset();
            }
            else
                hasCardTop[0][0] = false;
        }
    }
    public boolean DrawToTable(int toCol) {

        if (isValidMove(toCol)) {

            int topCard = findFirstCard(toCol) + 1;

            subFromDrawPile();
            subDeckSize();

            if (!hasCardTop[1][1])
                hasCardTop[0][1] = false;

            cardsOnTable[topCard][toCol] = up[0];
            hasCardTable[topCard][toCol] = true;

            DrawCycler();
        }

        return isValidMove(toCol);
    }
    public boolean DrawToFoundation(String suit) {

        //System.out.println("DrawToFoundation called");
        //System.out.println(suit);

        if (isValidMove(suit)) {
            subFromDrawPile();
            subDeckSize();
            switch (suit) {

                case "hearts" -> {
                    hearts.push(up[0]);
                    hasCardTop[0][3] = true;
                }
                case "diamonds" -> {
                    diamonds.push(up[0]);
                    hasCardTop[0][4] = true;
                }
                case "spades" -> {
                    spades.push(up[0]);
                    hasCardTop[0][5] = true;
                }
                case "clubs" -> {
                    clubs.push(up[0]);
                    hasCardTop[0][6] = true;
                }
            }
            DrawCycler();
        }

        return isValidMove(suit);
    }


    public void DrawCycler() {
        //System.out.println("DrawCycler called");
        for (int i = 0; i < 2; i++) {
            up[i] = up[i + 1];
        }
        up[2] = null;

        for (int i = 0; i < 3; i++) {
            hasCardTop[i][1] = i < stackSize();
            //System.out.println(i < stackSize());
        }
    }

    public boolean moveCard(int fromRow, int fromCol, int toCol) {

        //System.out.println("moveCard called.");

        int toCardCounter = findFirstCard(toCol) + 1;

        //System.out.println(toCardCounter);
        if (isValidMove(fromRow, fromCol, toCol)){

            //Step 1. Flip previous card if it exists
            if (fromRow > 0) { //Flips over previous card
                if (hasCardTable[fromRow - 1][fromCol] && cardsOnTable[fromRow - 1][fromCol].isDown()) {
                    cardsOnTable[fromRow - 1][fromCol].Flip();
                }
            }

            //Step 2. Move stack
            //System.out.println("fromRow: " + fromRow + ", fromCol: " + fromCol + ", toRow: " + toCardCounter + ", toCol: " + toCol);
            while (hasCardTable[fromRow][fromCol]) {
                //System.out.println("test");
                cardsOnTable[toCardCounter][toCol] = cardsOnTable[fromRow][fromCol];
                hasCardTable[toCardCounter++][toCol] = true;
                hasCardTable[fromRow][fromCol] = false;
                cardsOnTable[fromRow++][fromCol] = null;
            }
        }
        return isValidMove(fromRow, fromCol, toCol);
    }

    public boolean TableIntoFoundation(int fromRow, int fromCol, String suit) {
        //System.out.println("moveIntoFoundation called.");
        //System.out.println("TABLE.TableIntoFoundation(" + fromRow + ", " + fromCol + ", " + suit);
        //System.out.println("\nfromRow: " + fromRow + ", fromCol: " + fromCol + ", suit: " + suit);
        //System.out.print("Card trying to move: ");
        //cardsOnTable[fromRow][fromCol].Display();
        //System.out.println();
        //System.out.println("suit: |" + suit + "| = |" + cardsOnTable[fromRow][fromCol].getSuit() + '|');
        //System.out.println(cardsOnTable[fromRow][fromCol].getSuit() + '|');

        if (isValidMove(fromRow, fromCol, suit)) {

            //Flips previous card if exists
            if (fromRow > 0) { //Flips over previous card
                if (hasCardTable[fromRow - 1][fromCol] && cardsOnTable[fromRow - 1][fromCol].isDown())
                    cardsOnTable[fromRow - 1][fromCol].Flip();
            }


            switch (suit) {

                case "hearts" -> {
                    hearts.push(cardsOnTable[fromRow][fromCol]);
                    hasCardTop[0][3] = true;
                    hasCardTable[fromRow][fromCol] = false;
                    cardsOnTable[fromRow][fromCol] = null;
                }
                case "diamonds" -> {
                    diamonds.push(cardsOnTable[fromRow][fromCol]);
                    hasCardTop[0][4] = true;
                    cardsOnTable[fromRow][fromCol] = null;
                    hasCardTable[fromRow][fromCol] = false;
                }
                case "spades" -> {
                    spades.push(cardsOnTable[fromRow][fromCol]);
                    hasCardTop[0][5] = true;
                    cardsOnTable[fromRow][fromCol] = null;
                    hasCardTable[fromRow][fromCol] = false;
                }
                case "clubs" -> {
                    clubs.push(cardsOnTable[fromRow][fromCol]);
                    hasCardTop[0][6] = true;
                    cardsOnTable[fromRow][fromCol] = null;
                    hasCardTable[fromRow][fromCol] = false;
                }
            }
        }
        return isValidMove(fromRow, fromCol, suit);
    }
    public boolean FoundationToTable(int pile, int toCol){
        //System.out.println("Foundation to table called.");
        //System.out.println("FoundationToTable(" + pile + ", " + toCol + ")");
        Card temp = null;
        int toCardCounter = findFirstCard(toCol) + 1;
        if (isValidMove(pile, toCol)){

            //Step 2. Get card
            switch (pile) {
                case 3:
                    temp = hearts.peek();
                    hearts.pop();
                    if (hearts.size() == 0)
                        hasCardTop[0][pile] = false;
                    break;
                case 4:
                    temp = diamonds.peek();
                    diamonds.pop();
                    if (hearts.size() == 0)
                        hasCardTop[0][pile] = false;
                    break;
                case 5:
                    temp = spades.peek();
                    spades.pop();
                    if (hearts.size() == 0)
                        hasCardTop[0][pile] = false;
                    break;
                case 6:
                    temp = clubs.peek();
                    clubs.pop();
                    if (hearts.size() == 0)
                        hasCardTop[0][pile] = false;
                    break;
            }
            //Step 3. Place card
            cardsOnTable[toCardCounter][toCol] = temp;
            hasCardTable[toCardCounter][toCol] = true;
        }
        return isValidMove(pile, toCol);
    }

    public void CHEAT_DeleteDeck(){
        int temp = getDeckSize();
        for (int i = 0; i < temp; i++){
            deck.pop();
            subDeckSize();
        }
        hasCardTop[0][0] = false;
    }
    public void CHEAT_FlipAllCards(){
        for (int i = 0; i < MAX_ROWS; i++){
            for (int j = 0; j < MAX_COLUMNS; j++){
                if (hasCardTable[i][j] && cardsOnTable[i][j].isDown())
                    cardsOnTable[i][j].Flip();
            }
        }
    }
    public void CHEAT_PumpFoundations(){
        for (int i = 0; i < 13; i++){
            Card temp = new Card(69,420);
            hearts.push(temp);
            diamonds.push(temp);
            spades.push(temp);
            clubs.push(temp);
        }
    }
    */

}

