/*
 * Indexer.h
 *
 *  Created on: 08.03.2013
 *      Author: jauseg
 */

#ifndef INDEXER_H_
#define INDEXER_H_

struct TwoLinkedList
{
		void *object;
		TwoLinkedList *prev;
		TwoLinkedList *next;

		TwoLinkedList()
		{
			object = 0;
			prev = 0;
			next = 0;
		}

		/**
		 * insert new item after this item
		 *
		 * @param item
		 */
		void insert(TwoLinkedList *item)
		{
			item->prev = this;
			item->next = next;

			if (next)
			{
				next->prev = item;
			}

			next = item;
		}

		TwoLinkedList* remove()
		{
			if (prev)
			{
				prev->next = next;
			}

			if (next)
			{
				next->prev = prev;
			}

			prev = 0;
			next = 0;

			return this;
		}
};

class Indexer
{
	private:

		TwoLinkedList freeList;
		TwoLinkedList usedList;

		void freeTwoLinkedlist(TwoLinkedList *item)
		{
			while (item)
			{
				TwoLinkedList *tempItem = item;
				item = item->next;
				delete tempItem;
			}
		}

	public:
		Indexer();
		virtual ~Indexer();

		void free()
		{
			freeTwoLinkedlist(freeList.next);
			freeList.next = 0;
			freeTwoLinkedlist(usedList.next);
			usedList.next = 0;
		}

		TwoLinkedList* allocate()
		{
			TwoLinkedList *item = freeList.next;

			if (item == 0)
			{
				item = new TwoLinkedList();
			}
			else
			{
				item->remove();
			}

			usedList.insert(item);

			return item;
		}

		void release(TwoLinkedList *item)
		{
			item->remove();
			freeList.insert(item);
		}

		TwoLinkedList* getFirst()
		{
			return usedList.next;
		}

		TwoLinkedList* releaseAndGetNext(TwoLinkedList* item)
		{
			TwoLinkedList *rValue = item->next;
			item->remove();
			freeList.insert(item);
			return rValue;
		}

};

#endif /* INDEXER_H_ */
