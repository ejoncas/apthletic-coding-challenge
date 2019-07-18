package com.apthletic.codingchallenge;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

public class RobotExample {

    @Test
    public void test() {
        Board board = new Board(7);
        board.doSpiralWay();
    }

    private enum Direction {
        UP(1),
        RIGHT(2),
        DOWN(3),
        LEFT(4);

        private final int direction;

        Direction(int i) {
            this.direction = i;
        }

        public Direction next() {
            switch (this) {
                case UP:
                    return RIGHT;
                case RIGHT:
                    return DOWN;
                case DOWN:
                    return LEFT;
                case LEFT:
                    return UP;
                default:
                    throw new IllegalArgumentException("Not supported");
            }
        }

        public Direction reverse() {
            switch (this) {
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                default:
                    throw new IllegalArgumentException("Not supported");
            }
        }

    }

    private static final class Board {

        private static final List<Direction> LAP_ORDER = ImmutableList.of(
                Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP
        );

        private static final class Position {
            private int x;
            private int y;

            private Position(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                        .add("x", x)
                        .add("y", y)
                        .toString();
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Position position = (Position) o;
                return x == position.x &&
                        y == position.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }

        private int minX;
        private int maxX;
        private int minY;
        private int maxY;

        private Position currentPosition = new Position(0, 0);

        private Board(int boardSize) {
            //We start with a square
            this.minX = 0;
            this.maxX = boardSize;
            this.minY = 0;
            this.maxY = boardSize;
        }

        public void resizeX(int minX, int maxX) {
            this.minX = minX;
            this.maxX = maxX;
        }

        public void resizeY(int minY, int maxY) {
            this.minY = minY;
            this.maxY = maxY;
        }

        boolean canMove(Direction direction) {
            switch (direction) {
                case UP:
                    return currentPosition.y > minY;
                case RIGHT:
                    return currentPosition.x < maxX;
                case DOWN:
                    return currentPosition.y < maxY;
                case LEFT:
                    return currentPosition.x > minX;
                default:
                    throw new IllegalArgumentException("Not supported");
            }
        }

        public Position move(Direction direction) {
            if (canMove(direction)) {
                doMove(direction);
            }
            return currentPosition;
        }

        private void doMove(Direction direction) {
            switch (direction.direction) {
                case 1:
                    currentPosition.y = currentPosition.y - 1;
                    break;
                case 2:
                    currentPosition.x = currentPosition.x + 1;
                    break;
                case 3:
                    currentPosition.y = currentPosition.y + 1;
                    break;
                case 4:
                    currentPosition.x = currentPosition.x - 1;
                    break;
                default:
                    throw new IllegalArgumentException("Not supported");
            }
        }

        public void doLap() {
            for (Direction direction : LAP_ORDER) {
                while (canMove(direction)) {
                    System.out.println(move(direction));
                }
            }
        }

        public void doSnakeWay() {
            Direction dir = Direction.RIGHT;
            int finalXDestination = minX % 2 == 0 ? 0 : minX;
            do {
                while (canMove(dir)) {
                    System.out.println(move(dir));
                }
                System.out.println(move(Direction.DOWN));
                dir = dir.reverse();
            } while (currentPosition.y < minX || currentPosition.x != finalXDestination);
        }


        public void doSpiralWay() {
            Position center = new Position(maxX / 2, maxY / 2);
            Direction direction = Direction.RIGHT;
            while (!currentPosition.equals(center)) {
                while (canMove(direction)) {
                    System.out.println(move(direction));
                }
                shrinkBoard(direction);
                direction = direction.next();
                System.out.println();
                System.out.println("Going " + direction);
                System.out.println();
                //This move will  put the robot  back in the virtual board
                System.out.println(move(direction));
            }
        }

        private void shrinkBoard(Direction direction) {
            switch (direction) {
                case RIGHT:
                    resizeY(minY + 1, maxY);
                    break;
                case LEFT:
                    resizeY(minY, maxY - 1);
                    break;
                case UP:
                    resizeX(minX + 1, maxX);
                    break;
                case DOWN:
                    resizeX(minX, maxX - 1);
                    break;
            }
        }

    }


}
