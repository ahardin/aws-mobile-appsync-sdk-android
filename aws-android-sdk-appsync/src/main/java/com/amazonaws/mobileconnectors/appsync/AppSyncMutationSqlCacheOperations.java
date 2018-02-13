/**
 * Copyright 2018-2018 Amazon.com,
 * Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the
 * License. A copy of the License is located at
 *
 *     http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, express or implied. See the License
 * for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazonaws.mobileconnectors.appsync;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.LinkedList;
import java.util.List;

public final class AppSyncMutationSqlCacheOperations {
    private static final String INSERT_STATEMENT =
            String.format("INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)",
                    AppSyncMutationsSqlHelper.TABLE_MUTATION_RECORDS,
                    AppSyncMutationsSqlHelper.RECORD_IDENTIFIER,
                    AppSyncMutationsSqlHelper.COLUMN_RECORD,
                    AppSyncMutationsSqlHelper.RESPONSE_CLASS);

    private static final String DELETE_STATEMENT =
            String.format("DELETE FROM %s WHERE %s=?",
                    AppSyncMutationsSqlHelper.TABLE_MUTATION_RECORDS,
                    AppSyncMutationsSqlHelper.RECORD_IDENTIFIER);
    private static final String DELETE_ALL_RECORD_STATEMENT = String.format("DELETE FROM %s", AppSyncMutationsSqlHelper.TABLE_MUTATION_RECORDS);
    SQLiteDatabase database;
    private final SQLiteOpenHelper dbHelper;
    private final String[] allColumns = {AppSyncMutationsSqlHelper.COLUMN_ID,
            AppSyncMutationsSqlHelper.RECORD_IDENTIFIER,
            AppSyncMutationsSqlHelper.COLUMN_RECORD,
            AppSyncMutationsSqlHelper.RESPONSE_CLASS};

    private final SQLiteStatement insertStatement;
    private final SQLiteStatement deleteStatement;
    private final SQLiteStatement deleteAllRecordsStatement;

    AppSyncMutationSqlCacheOperations(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        database = dbHelper.getWritableDatabase();
        insertStatement = database.compileStatement(INSERT_STATEMENT);
        deleteStatement = database.compileStatement(DELETE_STATEMENT);
        deleteAllRecordsStatement = database.compileStatement(DELETE_ALL_RECORD_STATEMENT);
    }

    public void close() {
        dbHelper.close();
    }

    long createRecord(String recordIdentifier, String record, String responseClass) {
        insertStatement.bindString(1, recordIdentifier);
        insertStatement.bindString(2, record);
        insertStatement.bindString(3, responseClass);
        long recordId = insertStatement.executeInsert();
        return recordId;
    }

    boolean deleteRecord(String recordIdentifier) {
        deleteStatement.bindString(1, recordIdentifier);
        return deleteStatement.executeUpdateDelete() > 0;
    }

    List<PersistentOfflineMutationObject> fetchAllRecords() {
        LinkedList<PersistentOfflineMutationObject> mutationObjects = new LinkedList<>();
        Cursor cursor = database.query(AppSyncMutationsSqlHelper.TABLE_MUTATION_RECORDS,
                allColumns, null, null,
                null, null, AppSyncMutationsSqlHelper.COLUMN_ID);
        if (cursor == null || !cursor.moveToFirst()) {
            return mutationObjects;
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String recordIdentifier = cursor.getString(cursor.getColumnIndex(AppSyncMutationsSqlHelper.RECORD_IDENTIFIER));
                String record = cursor.getString(cursor.getColumnIndex(AppSyncMutationsSqlHelper.COLUMN_RECORD));
                String responseClass = cursor.getString(cursor.getColumnIndex(AppSyncMutationsSqlHelper.RESPONSE_CLASS));
                PersistentOfflineMutationObject mutationObject = new PersistentOfflineMutationObject(recordIdentifier, record, responseClass);
                mutationObjects.add(mutationObject);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return mutationObjects;
    }

    void clearCurrentCache() {
        deleteAllRecordsStatement.execute();
    }
}
