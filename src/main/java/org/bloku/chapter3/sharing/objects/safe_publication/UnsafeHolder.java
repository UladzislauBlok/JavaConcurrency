package org.bloku.chapter3.sharing.objects.safe_publication;

/**
 * You cannot rely on the integrity of partially constructed objects. An observing thread could see the object in an inconsistent state, and then later see its state suddenly change, even though it has not been modified since publication.
 *<p> In fact, if the Holder is published using the unsafe publication idiom in example below, and a thread other than the publishing thread were to call assertSanity, it could throw AssertionError!
 */
class UnsafeHolderPublication {
    public Holder holder;

    public void initialize() {
        holder = new Holder(42);
    }

    /**
     * Other threads could see an up-to-date value for the holder reference, but stale values for the state of the Holder.
     *<p> To make things even less predictable, a thread may see a stale value the first time it reads a field and then a more up-to-date value the next time, which is why assertSanity can throw AssertionError
     */
    private static class Holder {
        private int n;

        public Holder(int n) {
            this.n = n;
        }

        public void assertSanity() {
            if (n != n)
                throw new AssertionError("This statement is false.");
        }
    }
}
